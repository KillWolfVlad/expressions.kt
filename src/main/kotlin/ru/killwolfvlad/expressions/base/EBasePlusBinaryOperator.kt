package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBasePlusBinaryOperator : EBinaryOperator {
    override val description = "base plus binary operator"

    override val order = 2

    override val identifier = "+"
}
