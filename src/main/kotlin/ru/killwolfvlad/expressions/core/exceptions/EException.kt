package ru.killwolfvlad.expressions.core.exceptions

/**
 * Expression exception
 */
class EException(
    /**
     * Exception context
     */
    context: String,
    /**
     * Exception message
     */
    message: String,
) : Exception("$context: $message")
