package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Base instance
 */
abstract class BaseInstance : EInstance {
    override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance = (memory as BaseMemory).applier.applyBinaryOperator(this, other, expressionExecutor, memory, operator)

    override suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance = (memory as BaseMemory).applier.applyLeftUnaryOperator(this, expressionExecutor, memory, operator)

    override suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance = (memory as BaseMemory).applier.applyRightUnaryOperator(this, expressionExecutor, memory, operator)
}
