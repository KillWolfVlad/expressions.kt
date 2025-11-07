package ru.killwolfvlad.expressions.base.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import kotlin.reflect.KClass

/**
 * Base binary operator applier
 */
interface BaseBinaryOperatorApplier {
    /**
     * Supported operator
     */
    val operator: KClass<out EBinaryOperator>

    /**
     * Supported types
     */
    val types: List<Pair<KClass<out EInstance>, KClass<out EInstance>>>

    /**
     * Apply binary operator
     */
    suspend fun apply(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance
}
