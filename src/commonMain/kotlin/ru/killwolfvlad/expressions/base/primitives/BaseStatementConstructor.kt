package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor
import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Base statement class
 */
open class BaseStatementConstructor : EStatementConstructor {
    override val identifier = "Statement"

    override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: List<EToken>,
    ): EInstance = BaseStatementInstance(value)

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is BaseStringInstance -> expressionExecutor.parser.parse(argument.value)

                    is BaseStatementInstance -> argument.value

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return BaseStatementInstance(value)
        }
}
