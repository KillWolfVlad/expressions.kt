package ru.killwolfvlad.expressions.core.interfaces

import ru.killwolfvlad.expressions.core.types.EToken

/**
 * Expression parser
 */
interface EParser {
    /**
     * Parse expression
     */
    fun parse(expression: String): List<EToken>
}
