package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression class
 */
interface EClass : ESymbol {
    /**
     * Create instance
     */
    fun createInstance(arguments: List<Any>): EInstance
}
