package ru.killwolfvlad.expressions.core.interfaces

/**
 * Expression binary operator
 */
interface EBinaryOperator : ESymbol {
    /**
     * Binary operator order (the less, the more priority)
     */
    val order: Int
}
