package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Base function cache
 */
open class BaseFunctionCache(
    val arguments: Map<Int, String>,
    val tokens: List<EToken>,
)
