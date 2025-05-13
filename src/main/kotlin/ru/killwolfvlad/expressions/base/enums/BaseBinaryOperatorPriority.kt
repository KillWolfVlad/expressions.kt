package ru.killwolfvlad.expressions.base.enums

enum class BaseBinaryOperatorPriority(
    val value: Int,
) {
    MULTIPLY(100),
    PLUS(200),
    AND(300),
    OR(400),
    COMPARE(500),
}
