package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base less or equal binary operator
 */
class BaseLessOrEqualBinaryOperator : EBinaryOperator {
    override val description = "less or equal binary operator"

    override val identifier = "<="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
