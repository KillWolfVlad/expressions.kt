package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base exponentiation binary operator
 */
class BaseExponentiationBinaryOperator : EBinaryOperator {
    override val description = "exponentiation binary operator"

    override val identifier = "**"

    override val priority = BaseBinaryOperatorPriority.MULTIPLY.value
}
