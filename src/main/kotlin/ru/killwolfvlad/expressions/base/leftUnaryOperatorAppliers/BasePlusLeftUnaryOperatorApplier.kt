package ru.killwolfvlad.expressions.base.leftUnaryOperatorAppliers

import ru.killwolfvlad.expressions.base.interfaces.BaseLeftUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BasePlusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.primitives.BaseNumberInstance
import ru.killwolfvlad.expressions.base.primitives.BasePercentInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base plus left unary operator applier
 */
class BasePlusLeftUnaryOperatorApplier : BaseLeftUnaryOperatorApplier {
    override val operator = BasePlusLeftUnaryOperator::class

    override val types = listOf(BaseNumberInstance::class, BasePercentInstance::class)

    override suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = (value as BaseNumberInstance).let {
        BaseNumberInstance(it.value.plus(), it.scale, it.roundingMode)
    }
}
