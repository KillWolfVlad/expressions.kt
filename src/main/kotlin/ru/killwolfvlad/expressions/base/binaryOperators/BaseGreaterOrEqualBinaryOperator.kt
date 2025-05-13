package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base greater or equal binary operator
 */
class BaseGreaterOrEqualBinaryOperator : EBinaryOperator {
    override val description = "greater or equal binary operator"

    override val identifier = ">="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
