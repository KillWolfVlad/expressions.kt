package ru.killwolfvlad.expressions.core.symbols

/**
 * Expression binary operator
 */
interface EBinaryOperator : ESymbol {
    /**
     * Binary operator priority (the less, the more priority)
     */
    val priority: Int
}
