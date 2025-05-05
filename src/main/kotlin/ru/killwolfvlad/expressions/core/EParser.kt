package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.ETokenType
import ru.killwolfvlad.expressions.core.types.EToken

class EParser {
    companion object {
        private val SINGLE_CHAR_TOKENS_MAP =
            mapOf(
                ';' to ETokenType.SEMICOLON,
                '(' to ETokenType.OPEN_BRACKET,
                ')' to ETokenType.CLOSE_BRACKET,
            )
    }

    fun parse(expression: String): List<EToken> {
        val tokens = mutableListOf<EToken>()

        var currentTokenType: ETokenType? = null
        val currentTokenValue = StringBuilder()

        val clearCurrentToken = {
            currentTokenType = null
            currentTokenValue.clear()
        }

        val addCurrentToken = {
            if (currentTokenType != null) {
                tokens.add(EToken(currentTokenType!!, currentTokenValue.toString()))

                clearCurrentToken()
            }
        }

        expression.forEach { char ->
            if (SINGLE_CHAR_TOKENS_MAP.containsKey(char)) {
                addCurrentToken()

                tokens.add(EToken(SINGLE_CHAR_TOKENS_MAP[char]!!, char.toString()))
            } else if (char.isDigit() || char == ',' || char == '.') {
                val actualChar = if (char == ',') '.' else char

                if (currentTokenType == ETokenType.NUMBER) {
                    currentTokenValue.append(actualChar)

                    return@forEach
                }

                addCurrentToken()

                currentTokenType = ETokenType.NUMBER
                currentTokenValue.append(actualChar)
            } else if (char.isWhitespace()) {
                addCurrentToken()
            } else {
                if (currentTokenType == ETokenType.WORD) {
                    currentTokenValue.append(char)

                    return@forEach
                }

                addCurrentToken()

                currentTokenType = ETokenType.WORD
                currentTokenValue.append(char)
            }
        }

        addCurrentToken()

        return tokens
    }
}
