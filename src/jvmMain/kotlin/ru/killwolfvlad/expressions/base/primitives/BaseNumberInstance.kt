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
actual open class BaseNumberInstance(
    actual override val value: Any,
    protected val scale: Int,
    protected val roundingMode: RoundingMode,
) : EInstance {
    companion object {
        private val context = BaseNumberInstance::class.simpleName!!
    }

    inline val actualValue get() = value as BigDecimal

    actual override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance =
        baseValidateArgumentType<BaseNumberInstance, EInstance>(context, other) {
            if (it is BasePercentInstance) {
                when (operator) {
                    is BasePlusBinaryOperator -> return BaseNumberInstance(
                        actualValue
                            .plus(
                                actualValue.times(it.actualValue).setScale(scale, roundingMode),
                            ).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                    is BaseMinusBinaryOperator -> return BaseNumberInstance(
                        actualValue
                            .minus(
                                actualValue.times(it.actualValue).setScale(scale, roundingMode),
                            ).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )
                }
            }

            when (operator) {
                is BasePlusBinaryOperator ->
                    BaseNumberInstance(
                        actualValue.plus(it.actualValue).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseMinusBinaryOperator ->
                    BaseNumberInstance(
                        actualValue.minus(it.actualValue).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseMultiplyBinaryOperator ->
                    BaseNumberInstance(
                        actualValue.times(it.actualValue).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseDivideBinaryOperator ->
                    BaseNumberInstance(
                        actualValue.divide(it.actualValue, scale, roundingMode),
                        scale,
                        roundingMode,
                    )

                is BaseExponentiationBinaryOperator -> {
                    if (it.actualValue.stripTrailingZeros().scale() > 0) {
                        throw EException(context, "pow must be integer!")
                    }

                    BaseNumberInstance(
                        actualValue.pow(it.actualValue.toInt()).setScale(scale, roundingMode),
                        scale,
                        roundingMode,
                    )
                }

                is BaseGreaterBinaryOperator -> BaseBooleanInstance(actualValue > it.actualValue)

                is BaseGreaterOrEqualBinaryOperator -> BaseBooleanInstance(actualValue >= it.actualValue)

                is BaseLessBinaryOperator -> BaseBooleanInstance(actualValue < it.actualValue)

                is BaseLessOrEqualBinaryOperator -> BaseBooleanInstance(actualValue <= it.actualValue)

                is BaseEqualBinaryOperator -> BaseBooleanInstance(actualValue.compareTo(it.actualValue) == 0)

                is BaseNotEqualBinaryOperator -> BaseBooleanInstance(actualValue.compareTo(it.actualValue) != 0)

                else -> throw EException(
                    context,
                    "unsupported binary operator type ${operator::class.simpleName}!",
                )
            }
        }

    actual override suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance =
        when (operator) {
            is BasePlusLeftUnaryOperator -> BaseNumberInstance(actualValue.plus(), scale, roundingMode)

            is BaseMinusLeftUnaryOperator -> BaseNumberInstance(actualValue.negate(), scale, roundingMode)

            else -> throw EException(
                context,
                "unsupported left unary operator type ${operator::class.simpleName}!",
            )
        }

    actual override suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance =
        when (operator) {
            is BasePercentRightUnaryOperator ->
                BasePercentInstance(
                    actualValue
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
