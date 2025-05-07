package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

class EBaseDivideBinaryOperator : EBinaryOperator {
    override val description = "base divide binary operator"

    override val order = 1

    override val identifier = "/"
}
