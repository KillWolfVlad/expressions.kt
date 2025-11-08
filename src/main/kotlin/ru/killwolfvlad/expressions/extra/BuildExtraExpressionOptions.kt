package ru.killwolfvlad.expressions.extra

import ru.killwolfvlad.expressions.base.BaseExpressionOptionsBuilder
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.EOptions
import ru.killwolfvlad.expressions.extra.rightUnaryOperators.ExtraKiloRightUnaryOperator
import ru.killwolfvlad.expressions.extra.rightUnaryOperatorAppliers.ExtraKiloRightUnaryOperatorApplier

/**
 * Build extra expression options
 */
fun buildExtraExpressionOptions(block: BaseExpressionOptionsBuilder.() -> Unit = {}): EOptions =
    buildBaseExpressionOptions {
        add(ExtraKiloRightUnaryOperator())
        add(ExtraKiloRightUnaryOperatorApplier())

        block()
    }
