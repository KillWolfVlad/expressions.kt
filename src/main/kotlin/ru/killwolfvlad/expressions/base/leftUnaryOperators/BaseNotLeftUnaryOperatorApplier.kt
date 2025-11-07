package ru.killwolfvlad.expressions.base.leftUnaryOperators

import ru.killwolfvlad.expressions.base.interfaces.BaseLeftUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base not left unary operator applier
 */
class BaseNotLeftUnaryOperatorApplier : BaseLeftUnaryOperatorApplier {
    override val operator = BaseNotLeftUnaryOperator::class

    override val types = listOf(BaseBooleanInstance::class)

    override suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = BaseBooleanInstance((value as BaseBooleanInstance).value.not())
}
