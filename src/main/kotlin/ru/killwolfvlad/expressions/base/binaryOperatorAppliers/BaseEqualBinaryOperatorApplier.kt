package ru.killwolfvlad.expressions.base.binaryOperatorAppliers

import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.base.primitives.BaseNumberInstance
import ru.killwolfvlad.expressions.base.primitives.BasePercentInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base equal binary operator applier
 */
open class BaseEqualBinaryOperatorApplier : BaseBinaryOperatorApplier {
    override val operator = BaseEqualBinaryOperator::class

    override val types = listOf(
        BaseBooleanInstance::class to BaseBooleanInstance::class,
        BaseNumberInstance::class to BaseNumberInstance::class,
        BaseNumberInstance::class to BasePercentInstance::class,
        BasePercentInstance::class to BaseNumberInstance::class,
        BasePercentInstance::class to BasePercentInstance::class,
    )

    override suspend fun apply(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance {
        if (value is BaseBooleanInstance && other is BaseBooleanInstance) {
            return BaseBooleanInstance(value.value == other.value)
        }

        if (value is BaseNumberInstance && other is BaseNumberInstance) {
            return BaseBooleanInstance(value.value.compareTo(other.value) == 0)
        }

        throw NotImplementedError() // TODO: better error
    }
}
