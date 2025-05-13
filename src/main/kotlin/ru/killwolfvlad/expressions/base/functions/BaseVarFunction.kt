package ru.killwolfvlad.expressions.base.functions

import ru.killwolfvlad.expressions.base.classes.BaseStringInstance
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base var function
 */
class BaseVarFunction : EFunction {
    override val description = "var function"

    override val identifier = "var"

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance = execute(memory as BaseMemory, arguments)

    private inline fun execute(
        memory: BaseMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1, 2)) {
            baseValidateArgumentType<BaseStringInstance, EInstance>(identifier, arguments[0]) { key ->
                when (arguments.size) {
                    1 ->
                        memory.variables[key.value] ?: throw EException(
                            identifier,
                            "variable with name ${key.value} is not defined!",
                        )

                    2 -> {
                        memory.variables[key.value] = arguments[1]

                        arguments[1]
                    }

                    else -> throw EException(identifier, "unreachable code!")
                }
            }
        }
}
