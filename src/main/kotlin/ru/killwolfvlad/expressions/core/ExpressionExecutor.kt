package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.ETokenType
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.objects.EOpenBracket
import ru.killwolfvlad.expressions.core.objects.ESemicolon
import ru.killwolfvlad.expressions.core.types.EOptions
import ru.killwolfvlad.expressions.core.types.EToken

/**
 * Expression executor
 */
class ExpressionExecutor(
    val options: EOptions,
) {
    /**
     * Expression parser
     */
    val parser: ExpressionParser = ExpressionParser(options)

    /**
     * Execute expression
     */
    suspend fun execute(
        expression: String,
        memory: EMemory = options.memoryFactory(),
    ): EInstance = execute(parser.parse(expression), memory)

    /**
     * Execute expression
     */
    suspend fun execute(
        tokens: List<EToken>,
        memory: EMemory = options.memoryFactory(),
    ): EInstance {
        val instances = ArrayDeque<EInstance>()
        val operators = ArrayDeque<EToken>()

        for (token in tokens) {
            when (token.type) {
                ETokenType.SEMICOLON -> {
                    while (!operators.isEmpty() && operators.first().symbol != EOpenBracket && operators.first().symbol != ESemicolon) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    operators.addFirst(token)
                }

                ETokenType.OPEN_BRACKET -> operators.addFirst(token)

                ETokenType.CLOSE_BRACKET -> {
                    while (operators.first().symbol != EOpenBracket && operators.first().symbol != ESemicolon) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    if (operators.first().symbol == EOpenBracket) {
                        if (operators.size >= 2 && (operators[1].type == ETokenType.CALLABLE)) {
                            val arguments =
                                if (operators[1].callableWithoutArguments) {
                                    listOf()
                                } else {
                                    listOf(instances.removeFirst())
                                }

                            when (operators[1].symbol) {
                                is EClass ->
                                    instances.addFirst(
                                        (operators[1].symbol as EClass).createInstance(this, memory, arguments),
                                    )

                                is EFunction ->
                                    instances.addFirst(
                                        (operators[1].symbol as EFunction).execute(this, memory, arguments),
                                    )
                            }

                            operators.removeFirst() // remove open bracket
                            operators.removeFirst() // remove callable
                        } else {
                            operators.removeFirst() // remove open bracket
                        }
                    } else if (operators.first().symbol == ESemicolon) {
                        val arguments = ArrayDeque<EInstance>()

                        arguments.addFirst(instances.removeFirst())

                        while (operators.first().symbol != EOpenBracket) {
                            if (operators.first().symbol == ESemicolon) {
                                arguments.addFirst(instances.removeFirst())

                                operators.removeFirst()
                            } else {
                                throw Exception("unexpected operator ${operators.first().symbol::class.simpleName}!")
                            }
                        }

                        if (operators.size >= 2 && (operators[1].type == ETokenType.CALLABLE)) {
                            when (operators[1].symbol) {
                                is EClass ->
                                    instances.addFirst(
                                        (operators[1].symbol as EClass).createInstance(this, memory, arguments),
                                    )

                                is EFunction ->
                                    instances.addFirst(
                                        (operators[1].symbol as EFunction).execute(this, memory, arguments),
                                    )
                            }

                            operators.removeFirst() // remove open bracket
                            operators.removeFirst() // remove callable
                        } else {
                            throw Exception("missing callable name!")
                        }
                    }
                }

                ETokenType.PRIMITIVE ->
                    instances.addFirst(
                        (token.symbol as EClass)
                            .createInstance(this, memory, listOf(token.value)),
                    )

                ETokenType.BINARY_OPERATOR -> {
                    while (!operators.isEmpty() &&
                        operators.first().symbol.let { previousOperatorSymbol ->
                            previousOperatorSymbol is ELeftUnaryOperator ||
                                previousOperatorSymbol is EBinaryOperator &&
                                previousOperatorSymbol.priority <= (token.symbol as EBinaryOperator).priority
                        }
                    ) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    operators.addFirst(token)
                }

                ETokenType.LEFT_UNARY_OPERATOR -> operators.addFirst(token)

                ETokenType.RIGHT_UNARY_OPERATOR -> applyOperator(memory, instances, token)

                ETokenType.CALLABLE -> operators.addFirst(token)
            }
        }

        while (!operators.isEmpty() && operators.first().symbol != ESemicolon) {
            applyOperator(memory, instances, operators.removeFirst())
        }

        return instances.first()
    }

    private suspend inline fun applyOperator(
        memory: EMemory,
        instances: ArrayDeque<EInstance>,
        operator: EToken,
    ) {
        when (operator.symbol) {
            is EBinaryOperator -> {
                val right = instances.removeFirst()
                val left = instances.removeFirst()

                instances.addFirst(left.applyBinaryOperator(this, memory, right, operator.symbol))
            }

            is ELeftUnaryOperator -> {
                instances.addFirst(instances.removeFirst().applyLeftUnaryOperator(this, memory, operator.symbol))
            }

            is ERightUnaryOperator -> {
                instances.addFirst(instances.removeFirst().applyRightUnaryOperator(this, memory, operator.symbol))
            }

            else -> throw Exception("unknown operator ${operator.symbol::class.simpleName}!")
        }
    }
}
