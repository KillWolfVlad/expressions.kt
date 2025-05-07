package ru.killwolfvlad.expressions.core.objects

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.interfaces.ESymbol

/**
 * Expression open bracket
 */
object EOpenBracket : ESymbol {
    override val description: String = "open bracket"

    override val identifier: String = EReservedChar.OPEN_BRACKET.value.toString()
}
