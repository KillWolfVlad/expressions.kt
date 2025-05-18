package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EStringConstructor

/**
 * Base string class
 */
open class BaseStringConstructor : EStringConstructor {
    override val identifier = "String"

    override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: String,
    ): EInstance = BaseStringInstance(value)

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is BaseStatementInstance -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )

                    else -> argument.value.toString()
                }

            return BaseStringInstance(value)
        }
}
