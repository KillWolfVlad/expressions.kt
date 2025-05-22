package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression memory
 */
interface EMemory {
    /**
     * Create copy of current memory
     */
    fun copy(): EMemory
}
