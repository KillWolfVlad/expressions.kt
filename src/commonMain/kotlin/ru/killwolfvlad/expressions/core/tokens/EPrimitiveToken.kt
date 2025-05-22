package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.symbols.EPrimitiveConstructor
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor

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
    val primitiveConstructor: EPrimitiveConstructor<T>,
) : EToken {
    override fun toString() =
        when (primitiveConstructor) {
            is EStatementConstructor -> "${EReservedChar.LEFT_CURLY_BRACKET.value}...${EReservedChar.RIGHT_CURLY_BRACKET.value}"
            else -> value.toString()
        }
}
