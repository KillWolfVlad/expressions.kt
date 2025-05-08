package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.types.EMemory

/**
 * Expression class
 */
interface EClass : ESymbol {
    /**
     * Create instance
     */
    suspend fun createInstance(
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance
}
