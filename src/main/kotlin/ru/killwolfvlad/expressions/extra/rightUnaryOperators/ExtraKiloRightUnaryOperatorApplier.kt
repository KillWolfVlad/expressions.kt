package ru.killwolfvlad.expressions.extra.rightUnaryOperators

import ru.killwolfvlad.expressions.base.interfaces.BaseRightUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseNumberInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import java.math.BigDecimal

/**
 * Extra Kilo right unary operator applier
 */
open class ExtraKiloRightUnaryOperatorApplier : BaseRightUnaryOperatorApplier {
    override val operator = ExtraKiloRightUnaryOperator::class

    override val types = listOf(BaseNumberInstance::class)

    override suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = (value as BaseNumberInstance).let {
        BaseNumberInstance(
            it.value.times(BigDecimal(1000)).setScale(it.scale, it.roundingMode),
            it.scale,
            it.roundingMode,
        )
    }
}
