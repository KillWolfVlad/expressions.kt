package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.base.binaryOperators.BaseAndBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseDivideBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseExponentiationBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMinusBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMultiplyBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseOrBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.classes.BaseBooleanClass
import ru.killwolfvlad.expressions.base.classes.BaseNumberClass
import ru.killwolfvlad.expressions.base.classes.BaseStringClass
import ru.killwolfvlad.expressions.base.functions.BaseFunFunction
import ru.killwolfvlad.expressions.base.functions.BaseIfFunction
import ru.killwolfvlad.expressions.base.functions.BaseVarFunction
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseMinusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseNotLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BasePlusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.rightUnaryOperators.BasePercentRightUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.types.EOptions

/**
 * Build base expression options
 */
fun buildBaseExpressionOptions(
    numberClass: EClass = BaseNumberClass(),
    stringClass: EClass = BaseStringClass(),
    booleanClass: EClass = BaseBooleanClass(),
): EOptions =
    EOptions(
        binaryOperators =
            listOf(
                BasePlusBinaryOperator(),
                BaseMinusBinaryOperator(),
                BaseMultiplyBinaryOperator(),
                BaseDivideBinaryOperator(),
                BaseExponentiationBinaryOperator(),
                BaseOrBinaryOperator(),
                BaseAndBinaryOperator(),
                BaseGreaterBinaryOperator(),
                BaseGreaterOrEqualBinaryOperator(),
                BaseLessBinaryOperator(),
                BaseLessOrEqualBinaryOperator(),
                BaseEqualBinaryOperator(),
                BaseNotEqualBinaryOperator(),
            ),
        leftUnaryOperators =
            listOf(
                BasePlusLeftUnaryOperator(),
                BaseMinusLeftUnaryOperator(),
                BaseNotLeftUnaryOperator(),
            ),
        rightUnaryOperators = listOf(BasePercentRightUnaryOperator()),
        classes = listOf(numberClass, stringClass, booleanClass),
        functions = listOf(BaseIfFunction(), BaseVarFunction(), BaseFunFunction()),
        numberClass = numberClass,
        stringClass = stringClass,
        booleanClass = booleanClass,
        memoryFactory = { BaseMemory() },
    )
