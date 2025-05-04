package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression function
 */
interface EFunction {
    /**
     * Function name
     */
    val name: String

    /**
     * Function symbol
     */
    val symbol: String

    /**
     * Execute function
     */
    suspend fun execute(arguments: List<EClassInstance>): EClassInstance
}
