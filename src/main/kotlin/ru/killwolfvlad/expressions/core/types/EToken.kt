package ru.killwolfvlad.expressions.core.types

import ru.killwolfvlad.expressions.core.enums.ETokenType
import ru.killwolfvlad.expressions.core.interfaces.ESymbol

/**
 * Expression token
 */
data class EToken(
    val type: ETokenType,
    val value: String,
    val symbol: ESymbol,
)
