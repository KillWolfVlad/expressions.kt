package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base equal binary operator
 */
class BaseEqualBinaryOperator : EBinaryOperator {
    override val description = "equal binary operator"

    override val identifier = "=="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
