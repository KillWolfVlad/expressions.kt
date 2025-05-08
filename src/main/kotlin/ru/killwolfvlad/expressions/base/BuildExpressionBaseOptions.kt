package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.types.EOptions

fun buildExpressionBaseOptions(
    numberClass: EClass = EBaseNumberClass(),
    stringClass: EClass = EBaseStringClass(),
): EOptions =
    EOptions(
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
        classes = listOf(numberClass, stringClass),
        functions = listOf(EBaseVarFunction()),
        numberClass = numberClass,
        stringClass = stringClass,
    )
