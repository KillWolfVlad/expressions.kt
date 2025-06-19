package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.core.interfaces.EMemory

/**
 * Base memory
 */
open class BaseMemory(
    /**
     * Variables storage
     */
    val variables: MutableMap<String, BaseVariableRef> = mutableMapOf(),
    /**
     * Functions storage
     */
    val functions: MutableMap<String, BaseFunctionRef> = mutableMapOf(),
) : EMemory {
    /**
     * Create copy of current memory
     */
    open fun copy() =
        BaseMemory(
            variables.toMutableMap(),
            functions.toMutableMap(),
        )
}
