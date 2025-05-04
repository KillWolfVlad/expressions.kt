package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression binary operator
 */
interface EBinaryOperator {
    /**
     * Binary operator name
     */
    val name: String

    /**
     * Binary operator order (the less, the more priority)
     */
    val order: Int

    /**
     * Binary operator symbol
     */
    val symbol: String
}
