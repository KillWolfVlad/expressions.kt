package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.interfaces.EParser
import ru.killwolfvlad.expressions.core.types.EOptions

class ExpressionExecutor(
    private val options: EOptions,
    val parser: EParser = EParserImpl(options),
) {
    suspend fun execute(expression: String): Any =
        options.numberClass
            .createInstance(listOf("1"))
            .applyBinaryOperator(
                options.numberClass.createInstance(listOf("2")),
                options.binaryOperators.find { it.description == "plus-binary-operator" }!!,
            ).value
}
