package ru.killwolfvlad.expressions.base.binaryOperatorAppliers

import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base not equal binary operator applier
 */
open class BaseNotEqualBinaryOperatorApplier(
    val equalBinaryOperator: BaseEqualBinaryOperator,
) : BaseBinaryOperatorApplier {
    override val operator = BaseNotEqualBinaryOperator::class

    override val types = listOf(BaseBooleanInstance::class to BaseBooleanInstance::class)

    override suspend fun apply(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance {
        val equalResult = (memory as BaseMemory).applier.applyBinaryOperator(
            value,
            other,
            expressionExecutor,
            memory,
            equalBinaryOperator,
        )

        return BaseBooleanInstance((equalResult as BaseBooleanInstance).value.not())
    }
}
