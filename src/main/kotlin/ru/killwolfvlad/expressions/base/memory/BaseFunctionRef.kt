package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Base function reference
 */
open class BaseFunctionRef(
    val arguments: Map<Int, String>,
    val tokens: List<EToken>,
    val memory: BaseMemory,
)
