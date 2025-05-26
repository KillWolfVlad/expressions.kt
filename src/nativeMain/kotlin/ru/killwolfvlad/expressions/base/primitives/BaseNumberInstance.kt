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
import kotlin.math.pow

/**
 * Base number instance
 */
actual open class BaseNumberInstance(
    actual override val value: Any,
) : EInstance {
    companion object {
        private val context = BaseNumberInstance::class.simpleName!!
    }

    inline val actualValue get() = value as Double

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
                                actualValue.times(it.actualValue),
                            ),
                    )

                    is BaseMinusBinaryOperator -> return BaseNumberInstance(
                        actualValue
                            .minus(
                                actualValue.times(it.actualValue),
                            ),
                    )
                }
            }

            when (operator) {
                is BasePlusBinaryOperator ->
                    BaseNumberInstance(actualValue.plus(it.actualValue))

                is BaseMinusBinaryOperator ->
                    BaseNumberInstance(actualValue.minus(it.actualValue))

                is BaseMultiplyBinaryOperator ->
                    BaseNumberInstance(actualValue.times(it.actualValue))

                is BaseDivideBinaryOperator ->
                    BaseNumberInstance(actualValue.div(it.actualValue))

                is BaseExponentiationBinaryOperator -> {
                    BaseNumberInstance(
                        actualValue.pow(it.actualValue),
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
            is BasePlusLeftUnaryOperator -> BaseNumberInstance(actualValue.unaryPlus())

            is BaseMinusLeftUnaryOperator -> BaseNumberInstance(actualValue.unaryMinus())

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
                BasePercentInstance(actualValue.div(100.toDouble()))

            else -> throw EException(
                context,
                "unsupported right unary operator type ${operator::class.simpleName}!",
            )
        }
}
