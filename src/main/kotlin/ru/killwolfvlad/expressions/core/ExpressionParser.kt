package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.enums.ETokenType
import ru.killwolfvlad.expressions.core.interfaces.ESymbol
import ru.killwolfvlad.expressions.core.objects.ECloseBracket
import ru.killwolfvlad.expressions.core.objects.EOpenBracket
import ru.killwolfvlad.expressions.core.objects.ESemicolon
import ru.killwolfvlad.expressions.core.types.EOptions
import ru.killwolfvlad.expressions.core.types.EToken

/**
 * Expression parser
 */
class ExpressionParser(
    private val options: EOptions,
) {
    private data class ParsingContext(
        val tokens: MutableList<EToken> = mutableListOf(),
        // current token
        var currentTokenType: ETokenType? = null,
        val currentTokenValue: StringBuilder = StringBuilder(),
        var currentTokenSymbol: ESymbol? = null,
        // flags
        var isExpressionStart: Boolean = true,
        var isNumberAfterPoint: Boolean = false,
        var isStringAfterQuotationMark: Boolean = false,
        // brackets
        var bracketsCount: Int = 0,
    )

    companion object {
        private val SINGLE_CHAR_TOKENS_MAP =
            mapOf(
                EReservedChar.SEMICOLON.value to (ETokenType.SEMICOLON to ESemicolon),
                EReservedChar.OPEN_BRACKET.value to (ETokenType.OPEN_BRACKET to EOpenBracket),
                EReservedChar.CLOSE_BRACKET.value to (ETokenType.CLOSE_BRACKET to ECloseBracket),
            )

        private val DIGITS =
            setOf(
                EReservedChar.DIGIT_1.value,
                EReservedChar.DIGIT_2.value,
                EReservedChar.DIGIT_3.value,
                EReservedChar.DIGIT_4.value,
                EReservedChar.DIGIT_5.value,
                EReservedChar.DIGIT_6.value,
                EReservedChar.DIGIT_7.value,
                EReservedChar.DIGIT_8.value,
                EReservedChar.DIGIT_9.value,
                EReservedChar.DIGIT_0.value,
            )

        private val TOKEN_TYPES_WITH_SYMBOL =
            setOf(
                ETokenType.BINARY_OPERATOR,
                ETokenType.LEFT_UNARY_OPERATOR,
                ETokenType.RIGHT_UNARY_OPERATOR,
                ETokenType.CALLABLE,
            )

        private val FINAL_TOKEN_TYPES =
            setOf(
                ETokenType.SEMICOLON,
                ETokenType.CLOSE_BRACKET,
                ETokenType.PRIMITIVE,
                ETokenType.RIGHT_UNARY_OPERATOR,
            )

        private val TOKEN_POSITIONS_MAP =
            listOf(
                listOf(null, ETokenType.SEMICOLON) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                        ETokenType.PRIMITIVE,
                        ETokenType.LEFT_UNARY_OPERATOR,
                        ETokenType.CALLABLE,
                    ),
                listOf(ETokenType.OPEN_BRACKET) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                        ETokenType.CLOSE_BRACKET,
                        ETokenType.PRIMITIVE,
                        ETokenType.LEFT_UNARY_OPERATOR,
                        ETokenType.CALLABLE,
                    ),
                listOf(ETokenType.CLOSE_BRACKET) to
                    listOf(
                        ETokenType.SEMICOLON,
                        ETokenType.CLOSE_BRACKET,
                        ETokenType.BINARY_OPERATOR,
                        ETokenType.RIGHT_UNARY_OPERATOR,
                    ),
                listOf(ETokenType.PRIMITIVE) to
                    listOf(
                        ETokenType.CLOSE_BRACKET,
                        ETokenType.SEMICOLON,
                        ETokenType.BINARY_OPERATOR,
                        ETokenType.RIGHT_UNARY_OPERATOR,
                    ),
                listOf(ETokenType.LEFT_UNARY_OPERATOR) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                        ETokenType.PRIMITIVE,
                        ETokenType.CALLABLE,
                    ),
                listOf(ETokenType.RIGHT_UNARY_OPERATOR) to
                    listOf(
                        ETokenType.SEMICOLON,
                        ETokenType.CLOSE_BRACKET,
                        ETokenType.BINARY_OPERATOR,
                    ),
                listOf(ETokenType.BINARY_OPERATOR) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                        ETokenType.PRIMITIVE,
                        ETokenType.CALLABLE,
                    ),
                listOf(ETokenType.CALLABLE) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                    ),
            ).flatMap { it.first.map { key -> key to it.second.toSet() } }.toMap()
    }

    init {
        validateOptions()
    }

    private val binaryOperatorsMap = options.binaryOperators.associateBy { it.identifier }
    private val leftUnaryOperatorsMap = options.leftUnaryOperators.associateBy { it.identifier }
    private val rightUnaryOperatorsMap = options.rightUnaryOperators.associateBy { it.identifier }
    private val classesMap = options.classes.associateBy { it.identifier }
    private val functionsMap = options.functions.associateBy { it.identifier }

    /**
     * Parse expression
     */
    fun parse(expression: String): List<EToken> =
        ParsingContext().run {
            expression.forEach { char ->
                if (char == EReservedChar.QUOTATION_MARK.value) {
                    if (isStringAfterQuotationMark) {
                        addCurrentToken()
                    } else {
                        addCurrentToken()

                        currentTokenType = ETokenType.PRIMITIVE
                        currentTokenSymbol = options.stringClass

                        isStringAfterQuotationMark = true
                    }

                    return@forEach
                }

                if (isCurrentTokenString()) {
                    currentTokenValue.append(char)

                    return@forEach
                }

                if (char in SINGLE_CHAR_TOKENS_MAP) {
                    addCurrentToken()

                    val (singleCharTokenType, singleCharTokenSymbol) = SINGLE_CHAR_TOKENS_MAP[char]!!

                    currentTokenType = singleCharTokenType
                    currentTokenValue.append(char)
                    currentTokenSymbol = singleCharTokenSymbol

                    addCurrentToken()

                    if (singleCharTokenType == ETokenType.SEMICOLON || singleCharTokenType == ETokenType.OPEN_BRACKET) {
                        isExpressionStart = true
                    }

                    if (singleCharTokenType == ETokenType.OPEN_BRACKET) {
                        bracketsCount++
                    }

                    if (singleCharTokenType == ETokenType.CLOSE_BRACKET) {
                        bracketsCount--

                        if (bracketsCount < 0) {
                            throw Exception("wrong number of brackets!")
                        }
                    }
                } else if (char in DIGITS || char == EReservedChar.COMMA.value || char == EReservedChar.POINT.value) {
                    val actualChar = if (char == EReservedChar.COMMA.value) EReservedChar.POINT.value else char

                    if (actualChar == EReservedChar.POINT.value) {
                        if (isNumberAfterPoint) {
                            throw Exception("invalid number with double points!")
                        } else {
                            isNumberAfterPoint = true
                        }
                    }

                    if (isCurrentTokenNumber()) {
                        currentTokenValue.append(actualChar)

                        return@forEach
                    }

                    addCurrentToken()

                    currentTokenType = ETokenType.PRIMITIVE
                    currentTokenValue.append(actualChar)
                    currentTokenSymbol = options.numberClass
                } else if (char.isWhitespace()) {
                    addCurrentToken()

                    if (char == '\n' && tokens.isNotEmpty() && tokens[tokens.lastIndex].type != ETokenType.SEMICOLON) {
                        val (singleCharTokenType, singleCharTokenSymbol) = SINGLE_CHAR_TOKENS_MAP[EReservedChar.SEMICOLON.value]!!

                        currentTokenType = singleCharTokenType
                        currentTokenValue.append(EReservedChar.SEMICOLON.value)
                        currentTokenSymbol = singleCharTokenSymbol

                        addCurrentToken()
                    }
                } else {
                    if (currentTokenType != null && currentTokenType !in TOKEN_TYPES_WITH_SYMBOL) {
                        addCurrentToken()
                    }

                    currentTokenValue.append(char)

                    val findResult1 = findTokenTypeAndSymbol(isExpressionStart, currentTokenValue.toString())

                    if (findResult1 != null) {
                        currentTokenType = findResult1.first
                        currentTokenSymbol = findResult1.second
                    } else if (currentTokenType != null) {
                        currentTokenValue.let { currentTokenValue ->
                            currentTokenValue.deleteCharAt(currentTokenValue.length - 1)
                        }

                        addCurrentToken()

                        currentTokenValue.append(char)

                        val findResult2 = findTokenTypeAndSymbol(isExpressionStart, currentTokenValue.toString())

                        if (findResult2 != null) {
                            currentTokenType = findResult2.first
                            currentTokenSymbol = findResult2.second
                        }
                    }
                }
            }

            if (isCurrentTokenString()) {
                throw Exception("string without closing quotation mark!")
            }

            if (bracketsCount != 0) {
                throw Exception("wrong number of brackets!")
            }

            addCurrentToken()

            if (!(tokens.isNotEmpty() && tokens[tokens.lastIndex].type in FINAL_TOKEN_TYPES)) {
                throw Exception("expression is not completed!")
            }

            if (tokens[tokens.lastIndex].type == ETokenType.SEMICOLON) {
                tokens.removeAt(tokens.lastIndex)
            }

            if (tokens.isEmpty()) {
                throw Exception("expression can't be empty!")
            }

            return tokens
        }

    private inline fun findTokenTypeAndSymbol(
        isExpressionStart: Boolean,
        identifier: String,
    ): Pair<ETokenType, ESymbol>? {
        if (isExpressionStart) {
            if (identifier in leftUnaryOperatorsMap) {
                return ETokenType.LEFT_UNARY_OPERATOR to leftUnaryOperatorsMap[identifier]!!
            }
        } else {
            if (identifier in binaryOperatorsMap) {
                return ETokenType.BINARY_OPERATOR to binaryOperatorsMap[identifier]!!
            }

            if (identifier in rightUnaryOperatorsMap) {
                return ETokenType.RIGHT_UNARY_OPERATOR to rightUnaryOperatorsMap[identifier]!!
            }
        }

        if (identifier in classesMap) {
            return ETokenType.CALLABLE to classesMap[identifier]!!
        }

        if (identifier in functionsMap) {
            return ETokenType.CALLABLE to functionsMap[identifier]!!
        }

        return null
    }

    private inline fun validateOptions() {
        val reservedSymbols = EReservedChar.entries.map { it.value.toString() }.toSet()

        val binaryOperatorSymbols = options.binaryOperators.map { it.identifier }

        val leftUnaryOperatorSymbols = options.leftUnaryOperators.map { it.identifier }
        validateUnique(leftUnaryOperatorSymbols)

        val rightUnaryOperatorSymbols = options.rightUnaryOperators.map { it.identifier }

        val classSymbols = options.classes.map { it.identifier }

        val functionSymbols = options.functions.map { it.identifier }

        validateUnique(binaryOperatorSymbols + rightUnaryOperatorSymbols + classSymbols + functionSymbols)
        validateUnique(leftUnaryOperatorSymbols + classSymbols + functionSymbols)

        val userSymbols =
            listOf(
                binaryOperatorSymbols,
                leftUnaryOperatorSymbols,
                rightUnaryOperatorSymbols,
                classSymbols,
                functionSymbols,
                listOf(options.numberClass.identifier, options.stringClass.identifier),
            ).flatten()

        for (userSymbol in userSymbols) {
            if (userSymbol in reservedSymbols) {
                throw Exception("$userSymbol is reserved!")
            }
        }
    }

    private inline fun validateUnique(symbols: List<String>) {
        symbols.groupBy { it }.forEach {
            if (it.value.size > 1) {
                throw Exception("found duplicated symbol ${it.value.size}!")
            }
        }
    }

    private inline fun ParsingContext.clearCurrentToken() {
        currentTokenType = null
        currentTokenValue.clear()
        currentTokenSymbol = null

        isNumberAfterPoint = false
        isStringAfterQuotationMark = false
    }

    private inline fun ParsingContext.addCurrentToken() {
        if (currentTokenType == null && currentTokenValue.isNotEmpty()) {
            throw Exception("unknown identifier $currentTokenValue!")
        }

        if (currentTokenType != null) {
            tokens.add(
                EToken(
                    currentTokenType!!,
                    currentTokenValue.toString(),
                    currentTokenSymbol!!,
                ),
            )

            clearCurrentToken()

            isExpressionStart = false

            validateLastTokenPosition()
        }
    }

    private inline fun ParsingContext.validateLastTokenPosition() {
        val (token1, token2) =
            if (tokens.size == 1) {
                null to tokens[0]
            } else {
                tokens[tokens.lastIndex - 1] to tokens[tokens.lastIndex]
            }

        val validPositions = TOKEN_POSITIONS_MAP[token1?.type]!!

        if (token2.type !in validPositions) {
            throw Exception("position of ${token2.value} is not valid!")
        }

        if (token1?.type == ETokenType.OPEN_BRACKET && token2.type == ETokenType.CLOSE_BRACKET) {
            if (tokens.size == 2) {
                throw Exception("brackets can't be empty!")
            }

            val token0 = tokens[tokens.lastIndex - 2]

            if (token0.type == ETokenType.CALLABLE) {
                tokens[tokens.lastIndex - 2] = token0.copy(callableWithoutArguments = true)
            } else {
                throw Exception("brackets can't be empty!")
            }
        }
    }

    private inline fun ParsingContext.isCurrentTokenNumber(): Boolean =
        currentTokenType == ETokenType.PRIMITIVE && currentTokenSymbol == options.numberClass

    private inline fun ParsingContext.isCurrentTokenString(): Boolean =
        currentTokenType == ETokenType.PRIMITIVE && currentTokenSymbol == options.stringClass
}
