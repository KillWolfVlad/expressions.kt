package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base and binary operator
 */
open class BaseAndBinaryOperator : EBinaryOperator {
    override val identifier = "&&"

    override val priority = BaseBinaryOperatorPriority.AND.value
}
