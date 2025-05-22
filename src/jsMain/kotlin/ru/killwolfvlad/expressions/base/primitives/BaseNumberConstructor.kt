package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor

/**
 * Base number class
 */
actual open class BaseNumberConstructor : ENumberConstructor {
    actual override val identifier = "Number"

    actual override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: String,
    ): EInstance = BaseNumberInstance(value.toDouble())

    actual override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is BaseNumberInstance -> argument.actualValue

                    is BaseStringInstance -> argument.value.toDouble()

                    is BaseBooleanInstance -> (if (argument.value) 1 else 0).toDouble()

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return BaseNumberInstance(value)
        }
}
