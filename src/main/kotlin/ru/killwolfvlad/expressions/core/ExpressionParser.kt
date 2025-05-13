package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.enums.EReservedIdentifier
import ru.killwolfvlad.expressions.core.enums.ETokenType
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.ESymbol
import ru.killwolfvlad.expressions.core.objects.ECloseBracket
import ru.killwolfvlad.expressions.core.objects.EOpenBracket
import ru.killwolfvlad.expressions.core.objects.ESemicolon
import ru.killwolfvlad.expressions.core.types.EOptions
import ru.killwolfvlad.expressions.core.types.EToken

/**
 * Expression parser
 * TODO: add tests
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
        var expectedStringCloseChar: Char? = null,
        // brackets
        var bracketsCount: Int = 0,
    )

    companion object {
        private val context = ExpressionParser::class.simpleName!!

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

        private val FINAL_TOKEN_TYPES =
            setOf(
                ETokenType.SEMICOLON,
                ETokenType.CLOSE_BRACKET,
                ETokenType.PRIMITIVE,
                ETokenType.RIGHT_UNARY_OPERATOR,
            )

        private val TOKEN_TYPES_WITH_IDENTIFIER =
            setOf(
                ETokenType.BINARY_OPERATOR,
                ETokenType.LEFT_UNARY_OPERATOR,
                ETokenType.RIGHT_UNARY_OPERATOR,
                ETokenType.CALLABLE,
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
                listOf(ETokenType.CLOSE_BRACKET, ETokenType.PRIMITIVE) to
                    listOf(
                        ETokenType.SEMICOLON,
                        ETokenType.CLOSE_BRACKET,
                        ETokenType.BINARY_OPERATOR,
                        ETokenType.RIGHT_UNARY_OPERATOR,
                    ),
                listOf(ETokenType.BINARY_OPERATOR, ETokenType.LEFT_UNARY_OPERATOR) to
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
                listOf(ETokenType.CALLABLE) to
                    listOf(
                        ETokenType.OPEN_BRACKET,
                    ),
            ).flatMap { it.first.map { key -> key to it.second.toSet() } }.toMap()
    }

    private val binaryOperatorsMap = options.binaryOperators.associateBy { it.identifier }
    private val leftUnaryOperatorsMap = options.leftUnaryOperators.associateBy { it.identifier }
    private val rightUnaryOperatorsMap = options.rightUnaryOperators.associateBy { it.identifier }
    private val classesMap = options.classes.associateBy { it.identifier }
    private val functionsMap = options.functions.associateBy { it.identifier }

    init {
        validateOptions()
    }

    /**
     * Parse expression
     */
    fun parse(expression: String): List<EToken> = ParsingContext().parse(expression)

    private inline fun validateOptions() {
        val reservedIdentifiers =
            (EReservedChar.entries.map { it.value.toString() } + EReservedIdentifier.entries.map { it.value }).toSet()

        val binaryOperatorIdentifiers = options.binaryOperators.map { it.identifier }

        val leftUnaryOperatorIdentifiers = options.leftUnaryOperators.map { it.identifier }
        validateUnique(leftUnaryOperatorIdentifiers)

        val rightUnaryOperatorIdentifiers = options.rightUnaryOperators.map { it.identifier }

        val classIdentifiers = options.classes.map { it.identifier }

        val functionIdentifiers = options.functions.map { it.identifier }

        validateUnique(binaryOperatorIdentifiers + rightUnaryOperatorIdentifiers + classIdentifiers + functionIdentifiers)
        validateUnique(leftUnaryOperatorIdentifiers + classIdentifiers + functionIdentifiers)

        val userIdentifiers =
            listOf(
                binaryOperatorIdentifiers,
                leftUnaryOperatorIdentifiers,
                rightUnaryOperatorIdentifiers,
                classIdentifiers,
                functionIdentifiers,
                listOf(
                    options.numberClass.identifier,
                    options.stringClass.identifier,
                    options.booleanClass.identifier,
                ),
            ).flatten()

        for (userIdentifier in userIdentifiers) {
            if (userIdentifier in reservedIdentifiers) {
                throw EException(context, "$userIdentifier is reserved!")
            }
        }
    }

    private inline fun validateUnique(symbols: List<String>) {
        symbols.groupBy { it }.forEach {
            if (it.value.size > 1) {
                throw EException(context, "found duplicated identifier ${it.key}!")
            }
        }
    }

    private inline fun findTokenTypeAndSymbol(
        isExpressionStart: Boolean,
        identifier: String,
    ): Pair<ETokenType, ESymbol>? {
        if (identifier == EReservedIdentifier.TRUE.value || identifier == EReservedIdentifier.FALSE.value) {
            return ETokenType.PRIMITIVE to options.booleanClass
        }

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

    private inline fun ParsingContext.parse(expression: String): List<EToken> {
        expression.forEach f@{ char ->
            tryParseString(char) ?: return@f
            tryParseSingleCharToken(char) ?: return@f
            tryParseNumber(char) ?: return@f
            tryParseWhitespace(char) ?: return@f
            tryParseIdentifier(char)
        }

        if (isCurrentTokenString()) {
            throw EException(context, "string without closing!")
        }

        if (bracketsCount != 0) {
            throw EException(context, "wrong number of brackets!")
        }

        addCurrentToken()

        if (!(tokens.isNotEmpty() && tokens[tokens.lastIndex].type in FINAL_TOKEN_TYPES)) {
            throw EException(context, "expression is not completed!")
        }

        if (tokens[tokens.lastIndex].type == ETokenType.SEMICOLON) {
            tokens.removeAt(tokens.lastIndex)
        }

        if (tokens.isEmpty()) {
            throw EException(context, "expression can't be empty!")
        }

        return tokens
    }

    private inline fun ParsingContext.tryParseString(char: Char): Unit? {
        if (char == EReservedChar.QUOTATION_MARK.value ||
            char == EReservedChar.LEFT_CURLY_BRACKET.value ||
            char == EReservedChar.RIGHT_CURLY_BRACKET.value
        ) {
            if (isCurrentTokenString()) {
                if (char == expectedStringCloseChar) {
                    addCurrentToken()
                } else {
                    currentTokenValue.append(char)
                }
            } else {
                addCurrentToken()

                currentTokenType = ETokenType.PRIMITIVE
                currentTokenSymbol = options.stringClass

                expectedStringCloseChar =
                    when (char) {
                        EReservedChar.QUOTATION_MARK.value -> char

                        // TODO: parse {} as list of tokens
                        EReservedChar.LEFT_CURLY_BRACKET.value -> EReservedChar.RIGHT_CURLY_BRACKET.value

                        EReservedChar.RIGHT_CURLY_BRACKET.value ->
                            throw EException(context, "position of $char is not valid!")

                        else -> throw EException(context, "string can't be parsed!")
                    }
            }

            return null
        }

        if (isCurrentTokenString()) {
            currentTokenValue.append(char)

            return null
        }

        return Unit
    }

    private inline fun ParsingContext.tryParseSingleCharToken(char: Char): Unit? {
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
                    throw EException(context, "wrong number of brackets!")
                }
            }

            return null
        }

        return Unit
    }

    private inline fun ParsingContext.tryParseNumber(char: Char): Unit? {
        if (char in DIGITS || char == EReservedChar.COMMA.value || char == EReservedChar.POINT.value) {
            val actualChar = if (char == EReservedChar.COMMA.value) EReservedChar.POINT.value else char

            if (actualChar == EReservedChar.POINT.value) {
                if (isNumberAfterPoint) {
                    throw EException(context, "invalid number with double points!")
                } else {
                    isNumberAfterPoint = true
                }
            }

            if (isCurrentTokenNumber()) {
                currentTokenValue.append(actualChar)
            } else {
                addCurrentToken()

                currentTokenType = ETokenType.PRIMITIVE
                currentTokenValue.append(actualChar)
                currentTokenSymbol = options.numberClass
            }

            return null
        }

        return Unit
    }

    private inline fun ParsingContext.tryParseWhitespace(char: Char): Unit? {
        if (char.isWhitespace()) {
            addCurrentToken()

            val isLastTokenValidForAutoSemicolon =
                tokens.isNotEmpty() &&
                    tokens[tokens.lastIndex].type != ETokenType.SEMICOLON &&
                    tokens[tokens.lastIndex].type in FINAL_TOKEN_TYPES

            if (char == '\n' &&
                isLastTokenValidForAutoSemicolon &&
                bracketsCount == 0
            ) {
                val (singleCharTokenType, singleCharTokenSymbol) =
                    SINGLE_CHAR_TOKENS_MAP[EReservedChar.SEMICOLON.value]!!

                currentTokenType = singleCharTokenType
                currentTokenValue.append(EReservedChar.SEMICOLON.value)
                currentTokenSymbol = singleCharTokenSymbol

                addCurrentToken()
            }

            return null
        }

        return Unit
    }

    private inline fun ParsingContext.tryParseIdentifier(char: Char) {
        if (currentTokenType != null && currentTokenType !in TOKEN_TYPES_WITH_IDENTIFIER) {
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

    private inline fun ParsingContext.clearCurrentToken() {
        currentTokenType = null
        currentTokenValue.clear()
        currentTokenSymbol = null

        isNumberAfterPoint = false
        expectedStringCloseChar = null
    }

    private inline fun ParsingContext.addCurrentToken() {
        if (currentTokenType == null && currentTokenValue.isNotEmpty()) {
            throw EException(context, "unknown identifier $currentTokenValue!")
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
            throw EException(context, "position of ${token2.value} is not valid!")
        }

        if (token1?.type == ETokenType.OPEN_BRACKET && token2.type == ETokenType.CLOSE_BRACKET) {
            if (tokens.size == 2) {
                throw EException(context, "brackets can't be empty!")
            }

            val token0 = tokens[tokens.lastIndex - 2]

            if (token0.type == ETokenType.CALLABLE) {
                tokens[tokens.lastIndex - 2] = token0.copy(callableWithoutArguments = true)
            } else {
                throw EException(context, "brackets can't be empty!")
            }
        }
    }

    private inline fun ParsingContext.isCurrentTokenNumber(): Boolean =
        currentTokenType == ETokenType.PRIMITIVE && currentTokenSymbol == options.numberClass

    private inline fun ParsingContext.isCurrentTokenString(): Boolean =
        currentTokenType == ETokenType.PRIMITIVE && currentTokenSymbol == options.stringClass
}
