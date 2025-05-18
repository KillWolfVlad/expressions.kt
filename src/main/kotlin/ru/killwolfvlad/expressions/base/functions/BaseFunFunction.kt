package ru.killwolfvlad.expressions.base.functions

import ru.killwolfvlad.expressions.base.memory.BaseFunctionCache
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.primitives.BaseStatementInstance
import ru.killwolfvlad.expressions.base.primitives.BaseStringInstance
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Base fun function
 */
open class BaseFunFunction : EFunction {
    override val identifier = "fun"

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance = execute(expressionExecutor, memory as BaseMemory, arguments)

    private suspend inline fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: BaseMemory,
        arguments: List<EInstance>,
    ): EInstance {
        if (arguments.isEmpty()) {
            throw EException(identifier, "arguments count can't be 0!")
        }

        return baseValidateArgumentType<BaseStringInstance, EInstance>(identifier, arguments[0]) { functionName ->
            val functionCache = memory.functions[functionName.value]

            if (functionCache != null) {
                val functionMemory = (expressionExecutor.options.memoryFactory() as BaseMemory)

                memory.functions.forEach { functionMemory.functions[it.key] = it.value }

                arguments.subList(1, arguments.size).forEachIndexed { index, argument ->
                    val nameByPosition = functionCache.arguments[index]

                    if (nameByPosition != null) {
                        functionMemory.variables[nameByPosition] = argument
                    }
                }

                return expressionExecutor.execute(functionCache.tokens, functionMemory)
            } else {
                if (arguments.size == 1) {
                    throw EException(identifier, "arguments count can't be 1!")
                }

                memory.functions[functionName.value] =
                    BaseFunctionCache(
                        arguments =
                            arguments
                                .subList(1, arguments.lastIndex)
                                .mapIndexed { index, argument ->
                                    baseValidateArgumentType<BaseStringInstance, Pair<Int, String>>(
                                        identifier,
                                        argument,
                                    ) {
                                        index to it.value
                                    }
                                }.toMap(),
                        tokens =
                            baseValidateArgumentType<BaseStatementInstance, List<EToken>>(
                                identifier,
                                arguments[arguments.lastIndex],
                            ) {
                                it.value
                            },
                    )

                return functionName
            }
        }
    }
}
