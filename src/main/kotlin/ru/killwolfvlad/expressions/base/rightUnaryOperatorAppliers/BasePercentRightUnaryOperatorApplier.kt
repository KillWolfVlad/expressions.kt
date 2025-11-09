package ru.killwolfvlad.expressions.base.rightUnaryOperatorAppliers

import ru.killwolfvlad.expressions.base.interfaces.BaseRightUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseNumberInstance
import ru.killwolfvlad.expressions.base.primitives.BasePercentInstance
import ru.killwolfvlad.expressions.base.rightUnaryOperators.BasePercentRightUnaryOperator
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import java.math.BigDecimal

/**
 * Base percent right unary operator applier
 */
class BasePercentRightUnaryOperatorApplier : BaseRightUnaryOperatorApplier {
    override val operator = BasePercentRightUnaryOperator::class

    override val types = listOf(BaseNumberInstance::class, BasePercentInstance::class)

    override suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = (value as BaseNumberInstance).let {
        BasePercentInstance(
            it.value.divide(BigDecimal(100), it.scale * 2, it.roundingMode),
            it.scale,
            it.roundingMode,
        )
    }
}
