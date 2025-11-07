package ru.killwolfvlad.expressions.base.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import kotlin.reflect.KClass

/**
 * Base left unary operator applier
 */
interface BaseLeftUnaryOperatorApplier {
    /**
     * Supported operator
     */
    val operator: KClass<out ELeftUnaryOperator>

    /**
     * Supported types
     */
    val types: List<KClass<out EInstance>>

    /**
     * Apply left unary operator
     */
    suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance
}
