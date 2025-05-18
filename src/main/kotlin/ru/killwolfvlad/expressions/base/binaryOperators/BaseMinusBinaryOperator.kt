package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base minus binary operator
 */
open class BaseMinusBinaryOperator : EBinaryOperator {
    override val identifier = "-"

    override val priority = BaseBinaryOperatorPriority.PLUS.value

    override val associativity = EAssociativity.LR
}
