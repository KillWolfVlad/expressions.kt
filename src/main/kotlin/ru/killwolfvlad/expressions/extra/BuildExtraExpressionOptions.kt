package ru.killwolfvlad.expressions.extra

import ru.killwolfvlad.expressions.base.BaseExpressionOptionsBuilder
import ru.killwolfvlad.expressions.core.EOptions
import ru.killwolfvlad.expressions.extra.rightUnaryOperators.ExtraKiloRightUnaryOperator

/**
 * Build extra expression options
 */
fun buildExtraExpressionOptions(block: BaseExpressionOptionsBuilder.() -> Unit = {}): EOptions =
    BaseExpressionOptionsBuilder().apply {
        add(ExtraKiloRightUnaryOperator())
    }.apply(block).build()
