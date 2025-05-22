package ru.killwolfvlad.expressions.core.tokens

import ru.killwolfvlad.expressions.core.enums.EReservedChar

/**
 * Expression open bracket token
 */
object EOpenBracketToken : EToken {
    override fun toString() = EReservedChar.OPEN_BRACKET.value.toString()
}
