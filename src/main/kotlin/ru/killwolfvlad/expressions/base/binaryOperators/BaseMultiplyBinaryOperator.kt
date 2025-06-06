package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base multiply binary operator
 */
open class BaseMultiplyBinaryOperator : EBinaryOperator {
    override val identifier = "*"

    override val priority = BaseBinaryOperatorPriority.MULTIPLY.value

    override val associativity = EAssociativity.LR
}
