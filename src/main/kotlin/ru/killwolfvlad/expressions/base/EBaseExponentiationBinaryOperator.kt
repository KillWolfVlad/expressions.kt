package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBaseExponentiationBinaryOperator : EBinaryOperator {
    override val description = "base exponentiation binary operator"

    override val order = 1

    override val identifier = "**"
}
