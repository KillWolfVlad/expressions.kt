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
import ru.killwolfvlad.expressions.base.functions.BaseFunFunction
import ru.killwolfvlad.expressions.base.functions.BaseIfFunction
import ru.killwolfvlad.expressions.base.functions.BaseVarFunction
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseMinusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseNotLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BasePlusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseNumberConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseStatementConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseStringConstructor
import ru.killwolfvlad.expressions.base.rightUnaryOperators.BasePercentRightUnaryOperator
import ru.killwolfvlad.expressions.core.EOptions
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor
import ru.killwolfvlad.expressions.core.symbols.EStringConstructor

/**
 * Build base expression options
 */
fun buildBaseExpressionOptions(
    numberConstructor: ENumberConstructor = BaseNumberConstructor(),
    stringConstructor: EStringConstructor = BaseStringConstructor(),
    booleanConstructor: EBooleanConstructor = BaseBooleanConstructor(),
    statementConstructor: EStatementConstructor = BaseStatementConstructor(),
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
        functions =
            listOf(
                numberConstructor,
                stringConstructor,
                booleanConstructor,
                statementConstructor,
                BaseIfFunction(),
                BaseVarFunction(),
                BaseFunFunction(),
            ),
        numberConstructor = numberConstructor,
        stringConstructor = stringConstructor,
        booleanConstructor = booleanConstructor,
        statementConstructor = statementConstructor,
        memoryFactory = { BaseMemory() },
    )
