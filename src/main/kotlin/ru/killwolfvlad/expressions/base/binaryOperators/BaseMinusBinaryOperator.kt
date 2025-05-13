package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base minus binary operator
 */
class BaseMinusBinaryOperator : EBinaryOperator {
    override val description = "minus binary operator"

    override val identifier = "-"

    override val priority = BaseBinaryOperatorPriority.PLUS.value
}
