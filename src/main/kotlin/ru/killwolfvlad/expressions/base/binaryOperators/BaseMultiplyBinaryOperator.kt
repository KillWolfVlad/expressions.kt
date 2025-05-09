package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base multiply binary operator
 */
class BaseMultiplyBinaryOperator : EBinaryOperator {
    override val description = "multiply binary operator"

    override val identifier = "*"

    override val priority = BaseBinaryOperatorPriority.MULTIPLY.value
}
