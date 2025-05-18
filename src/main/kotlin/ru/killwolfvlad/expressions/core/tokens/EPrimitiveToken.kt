package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.symbols.EPrimitiveConstructor

/**
 * Expression primitive token
 */
data class EPrimitiveToken<T>(
    /**
     * Value
     */
    val value: T,
    /**
     * Primitive constructor
     */
    val constructor: EPrimitiveConstructor<T>,
) : EToken {
    override fun toString() = constructor.identifier
}
