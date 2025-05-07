package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.types.EOptions

fun buildExpressionBaseOptions(): EOptions {
    val baseNumberClass = EBaseNumberClass()
    val baseStringClass = EBaseStringClass()

    return EOptions(
        binaryOperators =
            listOf(
                EBasePlusBinaryOperator(),
                EBaseMinusBinaryOperator(),
                EBaseMultiplyBinaryOperator(),
                EBaseDivideBinaryOperator(),
                EBaseExponentiationBinaryOperator(),
            ),
        leftUnaryOperators =
            listOf(
                EBasePlusLeftUnaryOperator(),
                EBaseMinusLeftUnaryOperator(),
            ),
        rightUnaryOperators = listOf(),
        classes = listOf(baseNumberClass, baseStringClass),
        functions = emptyList(),
        numberClass = baseNumberClass,
        stringClass = baseStringClass,
    )
}
