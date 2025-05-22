package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.enums.EReservedChar

/**
 * Expression semicolon token
 */
object ESemicolonToken : EToken {
    override fun toString() = EReservedChar.SEMICOLON.value.toString()
}
