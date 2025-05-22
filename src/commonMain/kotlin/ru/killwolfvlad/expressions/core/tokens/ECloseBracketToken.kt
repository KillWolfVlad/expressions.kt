package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.enums.EReservedChar

/**
 * Expression close bracket token
 */
object ECloseBracketToken : EToken {
    override fun toString() = EReservedChar.CLOSE_BRACKET.value.toString()
}
