package ru.killwolfvlad.expressions.core.objects

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.interfaces.ESymbol

/**
 * Expression semicolon
 */
object ESemicolon : ESymbol {
    override val description = "semicolon"

    override val identifier = EReservedChar.SEMICOLON.value.toString()
}
