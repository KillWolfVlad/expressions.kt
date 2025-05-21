package ru.killwolfvlad.expressions.base.functions

import ru.killwolfvlad.expressions.base.extensions.expand
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EFunction

/**
 * Base if function
 */
open class BaseIfFunction : EFunction {
    override val identifier = "if"

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(3)) {
            baseValidateArgumentType<BaseBooleanInstance, EInstance>(
                identifier,
                arguments[0].expand(expressionExecutor, memory),
            ) { condition ->
                if (condition.value) {
                    arguments[1].expand(expressionExecutor, memory)
                } else {
                    arguments[2].expand(expressionExecutor, memory)
                }
            }
        }
}
