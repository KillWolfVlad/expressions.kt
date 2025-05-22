package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor

/**
 * Base boolean class
 */
expect open class BaseBooleanConstructor() : EBooleanConstructor {
    override val identifier: String

    override suspend fun createInstance(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        value: Boolean,
    ): EInstance

    override suspend fun execute(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance
}
