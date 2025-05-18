package ru.killwolfvlad.expressions.base.binaryOperators

import ru.killwolfvlad.expressions.base.enums.BaseBinaryOperatorPriority
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator

/**
 * Base exponentiation binary operator
 * TODO: add right association for exponentiation binary operator
 * TODO: see http://e-maxx.ru/algo/expressions_parsing for more info
 */
open class BaseExponentiationBinaryOperator : EBinaryOperator {
    override val identifier = "**"

    override val priority = BaseBinaryOperatorPriority.MULTIPLY.value
}
