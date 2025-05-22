package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor
import ru.killwolfvlad.expressions.core.symbols.EStringConstructor
import kotlin.js.JsExport

/**
 * Expression options
 */
@JsExport
data class EOptions(
    val binaryOperators: List<EBinaryOperator>,
    val leftUnaryOperators: List<ELeftUnaryOperator>,
    val rightUnaryOperators: List<ERightUnaryOperator>,
    val functions: List<EFunction>,
    val numberConstructor: ENumberConstructor,
    val stringConstructor: EStringConstructor,
    val booleanConstructor: EBooleanConstructor,
    val statementConstructor: EStatementConstructor,
    val memoryFactory: () -> EMemory,
)
