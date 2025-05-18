package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.binaryOperators.BaseDivideBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseExponentiationBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMinusBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMultiplyBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseMinusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BasePlusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.rightUnaryOperators.BasePercentRightUnaryOperator
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.plus

/**
 * Base number instance
 */
open class BaseNumberInstance(
    override val value: BigDecimal,
    private val scale: Int,
    private val roundingMode: RoundingMode,
) : EInstance {
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
                    // TODO: add fractional pow support
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

                is BaseEqualBinaryOperator -> BaseBooleanInstance(value.compareTo(it.value) == 0)

                is BaseNotEqualBinaryOperator -> BaseBooleanInstance(value.compareTo(it.value) != 0)

                else -> throw EException(
                    context,
                    "unsupported binary operator type ${operator::class.simpleName}!",
                )
            }
        }

    override suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance =
        when (operator) {
            is BasePlusLeftUnaryOperator -> BaseNumberInstance(value.plus(), scale, roundingMode)

            is BaseMinusLeftUnaryOperator -> BaseNumberInstance(value.negate(), scale, roundingMode)

            else -> throw EException(
                context,
                "unsupported left unary operator type ${operator::class.simpleName}!",
            )
        }

    override suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance =
        when (operator) {
            is BasePercentRightUnaryOperator ->
                BasePercentInstance(
                    value
                        .divide(
                            BigDecimal(100),
                            scale * 2,
                            roundingMode,
                        ),
                    scale,
                    roundingMode,
                )

            else -> throw EException(
                context,
                "unsupported right unary operator type ${operator::class.simpleName}!",
            )
        }
}
