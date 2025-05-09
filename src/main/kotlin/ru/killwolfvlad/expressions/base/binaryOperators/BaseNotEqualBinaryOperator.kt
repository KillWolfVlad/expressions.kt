package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base not equal binary operator
 */
class BaseNotEqualBinaryOperator : EBinaryOperator {
    override val description = "not equal binary operator"

    override val identifier = "!="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
