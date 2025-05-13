package ru.killwolfvlad.expressions.base.leftUnaryOperators

import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator

/**
 * Base not left unary operator
 */
class BaseNotLeftUnaryOperator : ELeftUnaryOperator {
    override val description = "base not left unary operator"

    override val identifier = "!"
}
