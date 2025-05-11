package ru.killwolfvlad.expressions.base.classes

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import java.math.BigDecimal

/**
 * Base boolean class
 */
open class BaseBooleanClass : EClass {
    override val description = "base boolean class"

    override val identifier = "Boolean"

    override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is String -> argument.toBooleanStrict()

                    is BaseNumberInstance -> argument.value.compareTo(BigDecimal.ZERO) != 0

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
