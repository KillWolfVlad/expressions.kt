package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base divide binary operator
 */
class BaseDivideBinaryOperator : EBinaryOperator {
    override val description = "divide binary operator"

    override val identifier = "/"

    override val priority = BaseBinaryOperatorPriority.MULTIPLY.value
}
