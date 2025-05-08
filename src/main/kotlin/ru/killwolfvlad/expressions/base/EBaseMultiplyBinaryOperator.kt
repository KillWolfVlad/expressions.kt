package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBaseMultiplyBinaryOperator : EBinaryOperator {
    override val description = "base multiply binary operator"

    override val priority = 10

    override val identifier = "*"
}
