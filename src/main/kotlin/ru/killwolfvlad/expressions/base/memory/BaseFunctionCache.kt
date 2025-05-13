package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.core.types.EToken

/**
 * Base function cache
 */
data class BaseFunctionCache(
    val arguments: Map<Int, String>,
    val tokens: List<EToken>,
)
