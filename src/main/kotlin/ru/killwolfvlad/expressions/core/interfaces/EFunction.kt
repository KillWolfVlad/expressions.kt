package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.types.EMemory

/**
 * Expression function
 */
interface EFunction : ESymbol {
    /**
     * Execute function
     */
    suspend fun execute(
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance
}
