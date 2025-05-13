package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base plus binary operator
 */
class BasePlusBinaryOperator : EBinaryOperator {
    override val description = "plus binary operator"

    override val identifier = "+"

    override val priority = BaseBinaryOperatorPriority.PLUS.value
}
