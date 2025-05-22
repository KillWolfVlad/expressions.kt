package ru.killwolfvlad.expressions.core.symbols

import ru.killwolfvlad.expressions.core.enums.EAssociativity

/**
 * Expression binary operator
 */
interface EBinaryOperator : ESymbol {
    /**
     * Binary operator priority (the less, the more priority)
     */
    val priority: Int

    /**
     * Binary operator associativity
     */
    val associativity: EAssociativity
}
