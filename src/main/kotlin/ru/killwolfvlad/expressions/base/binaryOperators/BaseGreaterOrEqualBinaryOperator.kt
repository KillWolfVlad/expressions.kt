package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base greater or equal binary operator
 */
open class BaseGreaterOrEqualBinaryOperator : EBinaryOperator {
    override val identifier = ">="

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
