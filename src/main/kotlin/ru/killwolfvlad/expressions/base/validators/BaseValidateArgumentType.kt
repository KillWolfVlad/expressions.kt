package ru.killwolfvlad.expressions.base.validators

import ru.killwolfvlad.expressions.core.exceptions.EException

/**
 * Base validate argument type
 */
inline fun <reified T : Any, R> baseValidateArgumentType(
    context: String,
    argument: Any,
    block: (argument: T) -> R,
): R {
    if (argument is T) {
        return block(argument)
    }

    throw EException(context, "argument type must be ${T::class.simpleName}!")
}
