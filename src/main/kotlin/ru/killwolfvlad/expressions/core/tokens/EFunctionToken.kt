package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.symbols.EFunction

/**
 * Expression function token
 */
data class EFunctionToken(
    /**
     * Function
     */
    val function: EFunction,
    /**
     * Function without arguments ("false" by default)
     */
    val withoutArguments: Boolean = false,
) : EToken {
    override fun toString() = if (withoutArguments) "${function.identifier}()" else "${function.identifier}(...)"
}
