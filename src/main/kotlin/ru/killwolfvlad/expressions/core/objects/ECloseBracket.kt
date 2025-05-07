package ru.killwolfvlad.expressions.core.objects

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.interfaces.ESymbol

/**
 * Expression close bracket
 */
object ECloseBracket : ESymbol {
    override val description: String = "close bracket"

    override val identifier: String = EReservedChar.CLOSE_BRACKET.value.toString()
}
