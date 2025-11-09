package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.binaryOperators.*
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Base number instance
 */
open class BaseNumberInstance(
    override val value: BigDecimal,
    val scale: Int,
    val roundingMode: RoundingMode,
) : BaseInstance() {
    companion object {
        private val context = BaseNumberInstance::class.simpleName!!
    }

    override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance =
        baseValidateArgumentType<BaseNumberInstance, EInstance>(context, other) {
            if (it is BasePercentInstance) {
                when (operator) {
                    is BasePlusBinaryOperator -> return BaseNumberInstance(
                        value
                            .plus(
                                value.times(it.value).setScale(scale, roundingMode),
                            ).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                    is BaseMinusBinaryOperator -> return BaseNumberInstance(
                        value
                            .minus(
                                value.times(it.value).setScale(scale, roundingMode),
                            ).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )
                }
            }

            when (operator) {
                is BasePlusBinaryOperator ->
                    BaseNumberInstance(
                        value.plus(it.value).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseMinusBinaryOperator ->
                    BaseNumberInstance(
                        value.minus(it.value).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseMultiplyBinaryOperator ->
                    BaseNumberInstance(
                        value.times(it.value).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseDivideBinaryOperator ->
                    BaseNumberInstance(
                        value.divide(it.value, scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseExponentiationBinaryOperator -> {
                    if (it.value.stripTrailingZeros().scale() > 0) {
                        throw EException(context, "pow must be integer!")
                    }

                    BaseNumberInstance(
                        value.pow(it.value.toInt()).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )
                }

                is BaseGreaterBinaryOperator -> BaseBooleanInstance(value > it.value)

                is BaseGreaterOrEqualBinaryOperator -> BaseBooleanInstance(value >= it.value)

                is BaseLessBinaryOperator -> BaseBooleanInstance(value < it.value)

                is BaseLessOrEqualBinaryOperator -> BaseBooleanInstance(value <= it.value)

                else -> throw EException(
                    context,
                    "unsupported binary operator type ${operator::class.simpleName}!",
                )
            }
        }
}
