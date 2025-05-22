package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator

/**
 * Expression left unary operator token
 */
data class ELeftUnaryOperatorToken(
    /**
     * Left unary operator
     */
    val operator: ELeftUnaryOperator,
) : EToken {
    override fun toString() = operator.identifier
}
