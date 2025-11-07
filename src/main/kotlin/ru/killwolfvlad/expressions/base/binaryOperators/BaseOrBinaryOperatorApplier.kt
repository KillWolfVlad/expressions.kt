package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base or binary operator applier
 */
open class BaseOrBinaryOperatorApplier : BaseBinaryOperatorApplier {
    override val operator = BaseOrBinaryOperator::class

    override val types = listOf(BaseBooleanInstance::class to BaseBooleanInstance::class)

    override suspend fun apply(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = BaseBooleanInstance((value as BaseBooleanInstance).value || (other as BaseBooleanInstance).value)
}
