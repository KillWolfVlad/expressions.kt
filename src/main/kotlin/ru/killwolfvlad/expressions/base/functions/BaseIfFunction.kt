package ru.killwolfvlad.expressions.base.functions

import ru.killwolfvlad.expressions.base.classes.BaseBooleanInstance
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base if function
 */
class BaseIfFunction : EFunction {
    override val description = "if function"

    override val identifier = "if"

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(3)) {
            baseValidateArgumentType<BaseBooleanInstance, EInstance>(identifier, arguments[0]) { condition ->
                if (condition.value) {
                    arguments[1]
                } else {
                    arguments[2]
                }
            }
        }
}
