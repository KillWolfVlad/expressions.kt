package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base not equal binary operator
 */
open class BaseNotEqualBinaryOperator : EBinaryOperator {
    override val identifier = "!="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
