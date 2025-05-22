package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Base number instance
 */
expect open class BaseNumberInstance : EInstance {
    override val value: Any

    override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance

    override suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance

    override suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance
}
