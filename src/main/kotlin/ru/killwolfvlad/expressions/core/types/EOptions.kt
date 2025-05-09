package ru.killwolfvlad.expressions.core.types

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator

/**
 * Expression options
 */
data class EOptions(
    val binaryOperators: List<EBinaryOperator>,
    val leftUnaryOperators: List<ELeftUnaryOperator>,
    val rightUnaryOperators: List<ERightUnaryOperator>,
    val classes: List<EClass>,
    val functions: List<EFunction>,
    val numberClass: EClass,
    val stringClass: EClass,
    val booleanClass: EClass,
)
