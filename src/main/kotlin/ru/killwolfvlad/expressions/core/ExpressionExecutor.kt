package ru.killwolfvlad.expressions.core

class ExpressionExecutor(
    private val options: ExpressionExecutorOptions,
) {
    suspend fun execute(expression: String): Any =
        options.numberClass
            .createInstance(listOf("1"))
            .applyBinaryOperator(
                options.numberClass.createInstance(listOf("2")),
                options.binaryOperators.find { it.name == "plus-binary-operator" }!!,
            ).value
}
