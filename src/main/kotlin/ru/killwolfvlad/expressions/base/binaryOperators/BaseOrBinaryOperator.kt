package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base or binary operator
 */
class BaseOrBinaryOperator : EBinaryOperator {
    override val description = "or binary operator"

    override val identifier = "||"

    override val priority = BaseBinaryOperatorPriority.OR.value
}
