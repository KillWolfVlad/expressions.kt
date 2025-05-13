package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator

/**
 * Base less binary operator
 */
class BaseLessBinaryOperator : EBinaryOperator {
    override val description = "less binary operator"

    override val identifier = "<"

    override val priority = BaseBinaryOperatorPriority.COMPARE.value
}
