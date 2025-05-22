package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base less binary operator
 */
open class BaseLessBinaryOperator : EBinaryOperator {
    override val identifier = "<"

    override val priority = BaseBinaryOperatorPriority.COMPARE.value

    override val associativity = EAssociativity.LR
}
