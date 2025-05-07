package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression function
 */
interface EFunction : ESymbol {
    /**
     * Execute function
     */
    suspend fun execute(arguments: List<EInstance>): EInstance
}
