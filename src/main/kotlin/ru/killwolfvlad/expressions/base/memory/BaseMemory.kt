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
    val functions: MutableMap<String, BaseFunctionCache> = mutableMapOf(),
) : EMemory

/**
 * Copy variables
 */
inline fun BaseMemory.copyVariables(from: BaseMemory): BaseMemory =
    this.apply {
        from.variables.forEach { variables[it.key] = it.value }
    }

/**
 * Copy functions
 */
inline fun BaseMemory.copyFunctions(from: BaseMemory): BaseMemory =
    this.apply {
        from.functions.forEach { functions[it.key] = it.value }
    }

/**
 * Copy all properties
 */
inline fun BaseMemory.copy(from: BaseMemory) =
    this
        .copyVariables(from)
        .copyFunctions(from)
