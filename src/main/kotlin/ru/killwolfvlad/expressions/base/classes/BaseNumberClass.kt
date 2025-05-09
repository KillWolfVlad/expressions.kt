package ru.killwolfvlad.expressions.base.classes

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.types.EMemory
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Base number class
 */
open class BaseNumberClass(
    private val scale: Int = 2,
    private val roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
) : EClass {
    override val description = "base number class"

    override val identifier = "Number"

    override suspend fun createInstance(
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is String -> BigDecimal(argument)

                    is BaseNumberInstance -> argument.value

                    is BaseStringInstance -> BigDecimal((argument).value)

                    is BaseBooleanInstance -> BigDecimal(if (argument.value) 1 else 0)

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return BaseNumberInstance(value.setScale(scale, roundingMode), scale, roundingMode)
        }
}
