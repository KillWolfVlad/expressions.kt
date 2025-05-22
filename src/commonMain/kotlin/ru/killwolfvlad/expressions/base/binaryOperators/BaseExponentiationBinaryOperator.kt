package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base exponentiation binary operator
 */
open class BaseExponentiationBinaryOperator : EBinaryOperator {
    override val identifier = "**"

    override val priority = BaseBinaryOperatorPriority.EXPONENTIATION.value

    override val associativity = EAssociativity.RL
}
