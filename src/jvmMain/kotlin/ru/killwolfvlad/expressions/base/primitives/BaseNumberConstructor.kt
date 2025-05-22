package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Base number class
 */
actual open class BaseNumberConstructor(
    protected val scale: Int,
    protected val roundingMode: RoundingMode,
) : ENumberConstructor {
    actual constructor() : this(2, RoundingMode.HALF_EVEN)

    actual override val identifier = "Number"

    actual override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: String,
    ): EInstance = createInstance(BigDecimal(value))

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

                    is BaseStringInstance -> BigDecimal(argument.value)

                    is BaseBooleanInstance -> BigDecimal(if (argument.value) 1 else 0)

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return createInstance(value)
        }

    private inline fun createInstance(value: BigDecimal): EInstance =
        BaseNumberInstance(value.setScale(scale, roundingMode), scale, roundingMode)
}
