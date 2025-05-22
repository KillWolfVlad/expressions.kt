package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Expression right unary operator token
 */
data class ERightUnaryOperatorToken(
    /**
     * Right unary operator
     */
    val operator: ERightUnaryOperator,
) : EToken {
    override fun toString() = operator.identifier
}
