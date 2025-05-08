package ru.killwolfvlad.expressions.core.types

/**
 * Expression memory
 */
data class EMemory(
    /**
     * Key-Value memory
     */
    val kv: MutableMap<String, Any> = mutableMapOf(),
)
