package ru.killwolfvlad.expressions.base.binaryOperatorAppliers

import ru.killwolfvlad.expressions.base.binaryOperators.BaseAndBinaryOperator
import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base and binary operator applier
 */
open class BaseAndBinaryOperatorApplier : BaseBinaryOperatorApplier {
    override val operator = BaseAndBinaryOperator::class

    override val types = listOf(BaseBooleanInstance::class to BaseBooleanInstance::class)

    override suspend fun apply(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance = BaseBooleanInstance((value as BaseBooleanInstance).value && (other as BaseBooleanInstance).value)
}
