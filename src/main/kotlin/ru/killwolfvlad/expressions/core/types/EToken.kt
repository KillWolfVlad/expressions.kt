package ru.killwolfvlad.expressions.core.types

import ru.killwolfvlad.expressions.core.enums.ETokenType

data class EToken(
    val type: ETokenType,
    val value: String,
)
