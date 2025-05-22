package ru.killwolfvlad.expressions.base.validators

import ru.killwolfvlad.expressions.core.exceptions.EException

/**
 * Base validate arguments count
 */
inline fun <T, R> baseValidateArgumentsCount(
    context: String,
    arguments: List<T>,
    validCounts: Set<Int>,
    block: () -> R,
): R {
    if (arguments.size !in validCounts) {
        throw EException(context, "arguments count must be ${validCounts.joinToString(", ")}!")
    }

    return block()
}
