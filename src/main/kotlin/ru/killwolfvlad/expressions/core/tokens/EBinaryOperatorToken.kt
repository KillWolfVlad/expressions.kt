package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Expression binary operator token
 */
data class EBinaryOperatorToken(
    /**
     * Binary operator
     */
    val operator: EBinaryOperator,
) : EToken {
    override fun toString() = operator.identifier
}
