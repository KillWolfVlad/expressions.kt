package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base or binary operator
 */
open class BaseOrBinaryOperator : EBinaryOperator {
    override val identifier = "||"

    override val priority = BaseBinaryOperatorPriority.OR.value

    override val associativity = EAssociativity.LR
}
