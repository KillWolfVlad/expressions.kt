package ru.killwolfvlad.expressions.core.enums

/**
 * Expression reserved char
 */
enum class EReservedChar(
    val value: Char,
) {
    SEMICOLON(';'),
    OPEN_BRACKET('('),
    CLOSE_BRACKET(')'),
    DIGIT_1('1'),
    DIGIT_2('2'),
    DIGIT_3('3'),
    DIGIT_4('4'),
    DIGIT_5('5'),
    DIGIT_6('6'),
    DIGIT_7('7'),
    DIGIT_8('8'),
    DIGIT_9('9'),
    DIGIT_0('0'),
    POINT('.'),
    COMMA(','),
    QUOTATION_MARK('"'),
    LEFT_CURLY_BRACKET('{'),
    RIGHT_CURLY_BRACKET('}'),
}
