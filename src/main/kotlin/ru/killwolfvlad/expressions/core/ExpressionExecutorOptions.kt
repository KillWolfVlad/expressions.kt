package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator

data class ExpressionExecutorOptions(
    val binaryOperators: List<EBinaryOperator>,
    val leftUnaryOperators: List<ELeftUnaryOperator>,
    val rightUnaryOperators: List<ERightUnaryOperator>,
    val classes: List<EClass>,
    val numberClass: EClass,
    val functions: List<EFunction>,
)
