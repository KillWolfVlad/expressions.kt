package ru.killwolfvlad.expressions.base.classes

import ru.killwolfvlad.expressions.base.binaryOperators.BaseAndBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseOrBinaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseNotLeftUnaryOperator
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.types.EMemory

/**
 * Base boolean instance
 */
open class BaseBooleanInstance(
    override val value: Boolean,
) : EInstance {
    companion object {
        private val context = BaseBooleanInstance::class.simpleName!!
    }

    override suspend fun applyBinaryOperator(
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance =
        baseValidateArgumentType<BaseBooleanInstance, EInstance>(context, other) {
            when (operator) {
                is BaseOrBinaryOperator -> BaseBooleanInstance(value || it.value)

                is BaseAndBinaryOperator -> BaseBooleanInstance(value && it.value)

                is BaseEqualBinaryOperator -> BaseBooleanInstance(value == it.value)

                is BaseNotEqualBinaryOperator -> BaseBooleanInstance(value != it.value)

                else -> throw EException(
                    context,
                    "unsupported binary operator type ${operator::class.simpleName}!",
                )
            }
        }

    override suspend fun applyLeftUnaryOperator(
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance =
        when (operator) {
            is BaseNotLeftUnaryOperator -> BaseBooleanInstance(value.not())

            else -> throw EException(
                context,
                "unsupported left unary operator type ${operator::class.simpleName}!",
            )
        }

    override suspend fun applyRightUnaryOperator(
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance =
        throw EException(
            context,
            "unsupported right unary operator type ${operator::class.simpleName}!",
        )
}
