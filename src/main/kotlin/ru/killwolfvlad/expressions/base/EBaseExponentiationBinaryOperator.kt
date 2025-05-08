package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBaseExponentiationBinaryOperator : EBinaryOperator {
    override val description = "base exponentiation binary operator"

    override val priority = 10

    override val identifier = "**"
}
