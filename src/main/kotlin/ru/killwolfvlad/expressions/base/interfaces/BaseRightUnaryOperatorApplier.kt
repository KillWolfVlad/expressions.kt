package ru.killwolfvlad.expressions.base.interfaces

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import kotlin.reflect.KClass

/**
 * Base right unary operator applier
 */
interface BaseRightUnaryOperatorApplier {
    /**
     * Supported operator
     */
    val operator: KClass<out ERightUnaryOperator>

    /**
     * Supported types
     */
    val types: List<KClass<out EInstance>>

    /**
     * Apply right unary operator
     */
    suspend fun apply(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
    ): EInstance
}
