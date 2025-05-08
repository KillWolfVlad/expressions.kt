package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBaseMinusBinaryOperator : EBinaryOperator {
    override val description = "base minus binary operator"

    override val priority = 20

    override val identifier = "-"
}
