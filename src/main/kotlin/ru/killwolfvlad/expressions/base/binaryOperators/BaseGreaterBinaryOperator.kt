package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base greater binary operator
 */
class BaseGreaterBinaryOperator : EBinaryOperator {
    override val description = "greater binary operator"

    override val identifier = ">"

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
