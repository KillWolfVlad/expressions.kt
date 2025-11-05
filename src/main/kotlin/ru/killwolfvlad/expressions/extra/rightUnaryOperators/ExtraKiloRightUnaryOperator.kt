package ru.killwolfvlad.expressions.extra.rightUnaryOperators

import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Extra Kilo right unary operator
 */
open class ExtraKiloRightUnaryOperator : ERightUnaryOperator {
    override val identifier = "K"

    override val aliases: List<String> = listOf("k", "К", "к")
}
