package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base memory
 */
open class BaseMemory(
    /**
     * Variables storage
     */
    val variables: MutableMap<String, EInstance> = mutableMapOf(),
) : EMemory
