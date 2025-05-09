package ru.killwolfvlad.expressions.core.types

/**
 * Expression memory
 */
data class EMemory(
    /**
     * Variables storage
     */
    val variables: MutableMap<String, Any> = mutableMapOf(),
)
