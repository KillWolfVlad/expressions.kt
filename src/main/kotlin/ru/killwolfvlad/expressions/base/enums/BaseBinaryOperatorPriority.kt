package ru.killwolfvlad.expressions.base.enums

enum class BaseBinaryOperatorPriority(
    val value: Int,
) {
    EXPONENTIATION(100),
    MULTIPLY(200),
    PLUS(300),
    COMPARE(400),
    EQUAL(500),
    AND(600),
    OR(700),
}
