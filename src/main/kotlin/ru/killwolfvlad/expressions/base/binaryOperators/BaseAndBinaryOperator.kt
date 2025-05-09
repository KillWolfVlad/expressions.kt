package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base and binary operator
 */
class BaseAndBinaryOperator : EBinaryOperator {
    override val description = "and binary operator"

    override val identifier = "&&"

    override val priority = BaseBinaryOperatorPriority.AND.value
}
