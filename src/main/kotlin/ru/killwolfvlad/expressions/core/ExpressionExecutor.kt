package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EPrimitiveConstructor
import ru.killwolfvlad.expressions.core.tokens.EBinaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ECloseBracketToken
import ru.killwolfvlad.expressions.core.tokens.EFunctionToken
import ru.killwolfvlad.expressions.core.tokens.ELeftUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.EOpenBracketToken
import ru.killwolfvlad.expressions.core.tokens.EPrimitiveToken
import ru.killwolfvlad.expressions.core.tokens.ERightUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ESemicolonToken
import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Expression executor
 */
class ExpressionExecutor(
    val options: EOptions,
) {
    companion object {
        private val context = ExpressionExecutor::class.simpleName!!
    }

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
    ): EInstance = processTokens(parser.parse(expression), memory)

    /**
     * Execute tokens
     */
    suspend fun execute(
        tokens: List<EToken>,
        memory: EMemory = options.memoryFactory(),
    ): EInstance = processTokens(tokens, memory)

    private suspend inline fun processTokens(
        tokens: List<EToken>,
        memory: EMemory,
    ): EInstance {
        val instances = ArrayDeque<EInstance>()
        val operators = ArrayDeque<EToken>()

        for (token in tokens) {
            when (token) {
                is ESemicolonToken -> {
                    while (!operators.isEmpty() && operators.first() !is EOpenBracketToken && operators.first() !is ESemicolonToken) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    operators.addFirst(token)
                }

                is EOpenBracketToken -> operators.addFirst(token)

                is ECloseBracketToken -> {
                    while (operators.first() !is EOpenBracketToken && operators.first() !is ESemicolonToken) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    if (operators.first() is EOpenBracketToken) {
                        if (operators.size >= 2 && (operators[1] is EFunctionToken)) {
                            val arguments =
                                if ((operators[1] as EFunctionToken).withoutArguments) {
                                    listOf()
                                } else {
                                    listOf(instances.removeFirst())
                                }

                            instances.addFirst(
                                (operators[1] as EFunctionToken).function.execute(this, memory, arguments),
                            )

                            operators.removeFirst() // remove open bracket
                            operators.removeFirst() // remove function
                        } else {
                            operators.removeFirst() // remove open bracket
                        }
                    } else if (operators.first() is ESemicolonToken) {
                        val arguments = ArrayDeque<EInstance>()

                        arguments.addFirst(instances.removeFirst())

                        while (operators.first() !is EOpenBracketToken) {
                            if (operators.first() is ESemicolonToken) {
                                arguments.addFirst(instances.removeFirst())

                                operators.removeFirst()
                            } else {
                                throw EException(context, "unexpected operator ${operators.first()}!")
                            }
                        }

                        if (operators.size >= 2 && (operators[1] is EFunctionToken)) {
                            instances.addFirst(
                                (operators[1] as EFunctionToken).function.execute(this, memory, arguments),
                            )

                            operators.removeFirst() // remove open bracket
                            operators.removeFirst() // remove function
                        } else {
                            throw EException(context, "missing function name!")
                        }
                    }
                }

                is EPrimitiveToken<*> ->
                    instances.addFirst(
                        (token.constructor as EPrimitiveConstructor<Any>).createInstance(this, memory, token.value as Any),
                    )

                is EBinaryOperatorToken -> {
                    while (canApplyOperator(operators, token)) {
                        applyOperator(memory, instances, operators.removeFirst())
                    }

                    operators.addFirst(token)
                }

                is ELeftUnaryOperatorToken -> operators.addFirst(token)

                is ERightUnaryOperatorToken -> applyOperator(memory, instances, token)

                is EFunctionToken -> operators.addFirst(token)
            }
        }

        while (!operators.isEmpty() && operators.first() !is ESemicolonToken) {
            applyOperator(memory, instances, operators.removeFirst())
        }

        return instances.first()
    }

    private inline fun canApplyOperator(
        operators: ArrayDeque<EToken>,
        currentOperatorToken: EBinaryOperatorToken,
    ): Boolean {
        if (operators.isEmpty()) {
            return false
        }

        val previousOperatorToken = operators.first()

        return when (previousOperatorToken) {
            is ELeftUnaryOperatorToken -> true

            is EBinaryOperatorToken ->
                when (currentOperatorToken.operator.associativity) {
                    EAssociativity.LR -> previousOperatorToken.operator.priority <= currentOperatorToken.operator.priority
                    EAssociativity.RL -> previousOperatorToken.operator.priority < currentOperatorToken.operator.priority
                }

            else -> false
        }
    }

    private suspend inline fun applyOperator(
        memory: EMemory,
        instances: ArrayDeque<EInstance>,
        operatorToken: EToken,
    ) {
        when (operatorToken) {
            is EBinaryOperatorToken -> {
                val right = instances.removeFirst()
                val left = instances.removeFirst()

                instances.addFirst(left.applyBinaryOperator(this, memory, right, operatorToken.operator))
            }

            is ELeftUnaryOperatorToken -> {
                instances.addFirst(instances.removeFirst().applyLeftUnaryOperator(this, memory, operatorToken.operator))
            }

            is ERightUnaryOperatorToken -> {
                instances.addFirst(instances.removeFirst().applyRightUnaryOperator(this, memory, operatorToken.operator))
            }

            else -> throw EException(context, "unknown operator token ${operatorToken::class.simpleName}!")
        }
    }
}
