package ru.killwolfvlad.expressions.core.symbols

/**
 * Expression symbol
 */
sealed interface ESymbol {
    /**
     * Identifier
     */
    val identifier: String

    /**
     * Aliases
     */
    val aliases: List<String>
        get() = emptyList()
}
