package ru.killwolfvlad.expressions.extra.rightUnaryOperators

import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Extra kilo right unary operator
 */
open class ExtraKiloRightUnaryOperator : ERightUnaryOperator {
    override val identifier = "k"

    override val aliases: List<String> = listOf("K", "К", "к")
}
