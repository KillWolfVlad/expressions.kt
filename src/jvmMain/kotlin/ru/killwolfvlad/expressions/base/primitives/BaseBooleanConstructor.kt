package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor
import java.math.BigDecimal

/**
 * Base boolean class
 */
actual open class BaseBooleanConstructor : EBooleanConstructor {
    actual override val identifier = "Boolean"

    actual override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: Boolean,
    ): EInstance = BaseBooleanInstance(value)

    actual override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is BaseNumberInstance -> argument.actualValue.compareTo(BigDecimal.ZERO) != 0

                    is BaseStringInstance -> argument.value.toBooleanStrict()

                    is BaseBooleanInstance -> argument.value

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return BaseBooleanInstance(value)
        }
}
