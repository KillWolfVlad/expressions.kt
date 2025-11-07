package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.base.binaryOperators.BaseAndBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseAndBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.binaryOperators.BaseDivideBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.binaryOperators.BaseExponentiationBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMinusBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseMultiplyBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.binaryOperators.BaseOrBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseOrBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.functions.BaseFunFunction
import ru.killwolfvlad.expressions.base.functions.BaseIfFunction
import ru.killwolfvlad.expressions.base.functions.BaseVarFunction
import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.interfaces.BaseLeftUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.interfaces.BaseRightUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseMinusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseNotLeftUnaryOperator
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BaseNotLeftUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.leftUnaryOperators.BasePlusLeftUnaryOperator
import ru.killwolfvlad.expressions.base.memory.BaseApplier
import ru.killwolfvlad.expressions.base.memory.BaseMemory
import ru.killwolfvlad.expressions.base.primitives.BaseBooleanConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseNumberConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseStatementConstructor
import ru.killwolfvlad.expressions.base.primitives.BaseStringConstructor
import ru.killwolfvlad.expressions.base.rightUnaryOperators.BasePercentRightUnaryOperator
import ru.killwolfvlad.expressions.core.EOptions
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor
import ru.killwolfvlad.expressions.core.symbols.EStringConstructor

/**
 * Base expression options builder
 */
class BaseExpressionOptionsBuilder {
    private val binaryOperators = mutableListOf(
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
    )

    private val leftUnaryOperators = mutableListOf(
        BasePlusLeftUnaryOperator(),
        BaseMinusLeftUnaryOperator(),
        BaseNotLeftUnaryOperator(),
    )

    private val rightUnaryOperators = mutableListOf<ERightUnaryOperator>(
        BasePercentRightUnaryOperator(),
    )

    private val functions = mutableListOf(
        BaseIfFunction(),
        BaseVarFunction(),
        BaseFunFunction(),
    )

    private var numberConstructor: ENumberConstructor = BaseNumberConstructor()
    private var stringConstructor: EStringConstructor = BaseStringConstructor()
    private var booleanConstructor: EBooleanConstructor = BaseBooleanConstructor()
    private var statementConstructor: EStatementConstructor = BaseStatementConstructor()

    private val baseBinaryOperatorAppliers = mutableListOf(
        BaseAndBinaryOperatorApplier(),
        BaseEqualBinaryOperatorApplier(),
        BaseNotEqualBinaryOperatorApplier(),
        BaseOrBinaryOperatorApplier(),
    )

    private val baseLeftUnaryOperatorAppliers = mutableListOf<BaseLeftUnaryOperatorApplier>(BaseNotLeftUnaryOperatorApplier())

    private val baseRightUnaryOperatorAppliers = mutableListOf<BaseRightUnaryOperatorApplier>()

    private var applier = lazy {
        BaseApplier(
            baseBinaryOperatorAppliers,
            baseLeftUnaryOperatorAppliers,
            baseRightUnaryOperatorAppliers,
        )
    }

    private var memoryFactory: () -> EMemory = { BaseMemory(applier.value) }

    fun add(value: EBinaryOperator) {
        binaryOperators.add(value)
    }

    fun add(value: ELeftUnaryOperator) {
        leftUnaryOperators.add(value)
    }

    fun add(value: ERightUnaryOperator) {
        rightUnaryOperators.add(value)
    }

    fun add(value: EFunction) {
        functions.add(value)
    }

    fun primitive(value: ENumberConstructor) {
        numberConstructor = value
    }

    fun primitive(value: EStringConstructor) {
        stringConstructor = value
    }

    fun primitive(value: EBooleanConstructor) {
        booleanConstructor = value
    }

    fun primitive(value: EStatementConstructor) {
        statementConstructor = value
    }

    fun add(value: BaseBinaryOperatorApplier) {
        baseBinaryOperatorAppliers.add(value)
    }

    fun add(value: BaseLeftUnaryOperatorApplier) {
        baseLeftUnaryOperatorAppliers.add(value)
    }

    fun add(value: BaseRightUnaryOperatorApplier) {
        baseRightUnaryOperatorAppliers.add(value)
    }

    fun memory(value: () -> EMemory) {
        memoryFactory = value
    }

    internal fun build(): EOptions {
        return EOptions(
            binaryOperators = binaryOperators,
            leftUnaryOperators = leftUnaryOperators,
            rightUnaryOperators = rightUnaryOperators,
            functions = listOf(
                numberConstructor,
                stringConstructor,
                booleanConstructor,
                statementConstructor,
            ) + functions,
            numberConstructor = numberConstructor,
            stringConstructor = stringConstructor,
            booleanConstructor = booleanConstructor,
            statementConstructor = statementConstructor,
            memoryFactory = memoryFactory,
        )
    }
}
