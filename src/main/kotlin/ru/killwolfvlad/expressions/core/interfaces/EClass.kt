package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression class
 */
interface EClass {
    /**
     * Class name
     */
    val name: String

    /**
     * Class symbol
     */
    val symbol: String

    /**
     * Create instance
     */
    fun createInstance(arguments: List<Any>): EClassInstance
}
