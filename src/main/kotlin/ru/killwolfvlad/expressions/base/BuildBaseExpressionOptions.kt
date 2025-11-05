package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.EOptions

/**
 * Build base expression options
 */
fun buildBaseExpressionOptions(block: BaseExpressionOptionsBuilder.() -> Unit = {}): EOptions =
    BaseExpressionOptionsBuilder().apply(block).build()
