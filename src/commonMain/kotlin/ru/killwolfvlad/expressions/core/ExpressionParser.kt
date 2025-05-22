package ru.killwolfvlad.expressions.core

import ru.killwolfvlad.expressions.core.enums.EReservedChar
import ru.killwolfvlad.expressions.core.enums.EReservedIdentifier
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.EBooleanConstructor
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ENumberConstructor
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.EStatementConstructor
import ru.killwolfvlad.expressions.core.symbols.EStringConstructor
import ru.killwolfvlad.expressions.core.symbols.ESymbol
import ru.killwolfvlad.expressions.core.tokens.EBinaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ECloseBracketToken
import ru.killwolfvlad.expressions.core.tokens.EFunctionToken
import ru.killwolfvlad.expressions.core.tokens.ELeftUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.EOpenBracketToken
import ru.killwolfvlad.expressions.core.tokens.EPrimitiveToken
import ru.killwolfvlad.expressions.core.tokens.ERightUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ESemicolonToken
import ru.killwolfvlad.expressions.core.tokens.EToken
import kotlin.js.JsExport
import kotlin.reflect.KClass

/**
 * Expression parser
 */
@JsExport
class ExpressionParser internal constructor(
    private val options: EOptions,
) {
    private data class ParsingContext(
        val tokensStack: ArrayDeque<MutableList<EToken>> = ArrayDeque(listOf(mutableListOf())),
        // current token
        var currentTokenType: KClass<out EToken>? = null,
        val currentTokenValue: StringBuilder = StringBuilder(),
        var currentTokenSymbol: ESymbol? = null,
        // flags
        var isComment: Boolean = false,
        var isExpressionStart: Boolean = true,
        var isNumberAfterPoint: Boolean = false,
        var isStringEscape: Boolean = false,
        // brackets
        val bracketsCountStack: ArrayDeque<Int> = ArrayDeque(listOf(0)),
        var curlyBracketsCount: Int = 0,
    )

    private enum class ParsingResult {
        NEXT_CHAR,
        NEXT_PARSER,
        RESTART_PARSERS,
    }

    companion object {
        private val context = ExpressionParser::class.simpleName!!

        private const val CR = '\r'
        private const val LF = '\n'

        private val SINGLE_CHAR_TOKENS_MAP =
            mapOf(
                EReservedChar.SEMICOLON.value to ESemicolonToken::class,
                EReservedChar.OPEN_BRACKET.value to EOpenBracketToken::class,
                EReservedChar.CLOSE_BRACKET.value to ECloseBracketToken::class,
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
                ESemicolonToken::class,
                ECloseBracketToken::class,
                EPrimitiveToken::class,
                ERightUnaryOperatorToken::class,
            )

        private val TOKEN_TYPES_WITH_IDENTIFIER =
            setOf(
                EBinaryOperatorToken::class,
                ELeftUnaryOperatorToken::class,
                ERightUnaryOperatorToken::class,
                EFunctionToken::class,
            )

        private val TOKEN_POSITIONS_MAP =
            listOf(
                listOf(null, ESemicolonToken::class) to
                    setOf(
                        EOpenBracketToken::class,
                        EPrimitiveToken::class,
                        ELeftUnaryOperatorToken::class,
                        EFunctionToken::class,
                    ),
                listOf(EOpenBracketToken::class) to
                    setOf(
                        EOpenBracketToken::class,
                        ECloseBracketToken::class,
                        EPrimitiveToken::class,
                        ELeftUnaryOperatorToken::class,
                        EFunctionToken::class,
                    ),
                listOf(ECloseBracketToken::class, EPrimitiveToken::class) to
                    setOf(
                        ESemicolonToken::class,
                        ECloseBracketToken::class,
                        EBinaryOperatorToken::class,
                        ERightUnaryOperatorToken::class,
                    ),
                listOf(EBinaryOperatorToken::class, ELeftUnaryOperatorToken::class) to
                    setOf(
                        EOpenBracketToken::class,
                        EPrimitiveToken::class,
                        EFunctionToken::class,
                    ),
                listOf(ERightUnaryOperatorToken::class) to
                    setOf(
                        ESemicolonToken::class,
                        ECloseBracketToken::class,
                        EBinaryOperatorToken::class,
                    ),
                listOf(EFunctionToken::class) to
                    setOf(
                        EOpenBracketToken::class,
                    ),
            ).flatMap { it.first.map { key -> key to it.second } }.toMap()
    }

    private val binaryOperatorsMap = options.binaryOperators.associateBy { it.identifier }
    private val leftUnaryOperatorsMap = options.leftUnaryOperators.associateBy { it.identifier }
    private val rightUnaryOperatorsMap = options.rightUnaryOperators.associateBy { it.identifier }
    private val functionsMap = options.functions.associateBy { it.identifier }

    init {
        validateOptions()
    }

    /**
     * Parse expression
     */
    fun parse(expression: String): List<EToken> = ParsingContext().parse(expression)

    private inline fun validateOptions() {
        val binaryOperatorIdentifiers = options.binaryOperators.map { it.identifier }
        val leftUnaryOperatorIdentifiers = options.leftUnaryOperators.map { it.identifier }
        val rightUnaryOperatorIdentifiers = options.rightUnaryOperators.map { it.identifier }
        val functionIdentifiers = options.functions.map { it.identifier }

        validateIdentifiersUniqueness(leftUnaryOperatorIdentifiers + functionIdentifiers)
        validateIdentifiersUniqueness(binaryOperatorIdentifiers + rightUnaryOperatorIdentifiers + functionIdentifiers)

        val identifiers = binaryOperatorIdentifiers + leftUnaryOperatorIdentifiers + rightUnaryOperatorIdentifiers + functionIdentifiers

        validateIdentifiers(identifiers)
    }

    private inline fun validateIdentifiersUniqueness(identifiers: List<String>) {
        identifiers.groupBy { it }.forEach {
            if (it.value.size > 1) {
                throw EException(context, "found duplicated identifier ${it.key}!")
            }
        }
    }

    private inline fun validateIdentifiers(identifiers: List<String>) {
        val reservedChars =
            EReservedChar.entries
                .map { it.value }
                .filter { it !in DIGITS }
                .toSet()

        val reservedIdentifiers = EReservedIdentifier.entries.map { it.value }.toSet()

        identifiers.forEach { identifier ->
            if (identifier.isEmpty()) {
                throw EException(context, "identifier can't be empty!")
            }

            if (identifier in reservedIdentifiers) {
                throw EException(context, "identifier $identifier is reserved!")
            }

            if (DIGITS.any { digit -> identifier.startsWith(digit) }) {
                throw EException(context, "identifier $identifier can't starts with digit!")
            }

            if (identifier.any { char -> char in reservedChars }) {
                throw EException(context, "identifier $identifier can't contains reserved char!")
            }

            if (identifier.any { char -> char.isWhitespace() }) {
                throw EException(context, "identifier $identifier can't contains whitespace!")
            }
        }
    }

    private inline fun ParsingContext.parse(expression: String): List<EToken> {
        expression.forEach { char ->
            parseChar(char)
        }

        if (isCurrentTokenString()) {
            throw EException(context, "string without closing quotation mark!")
        }

        if (curlyBracketsCount != 0) {
            throw EException(context, "wrong number of curly brackets!")
        }

        addCurrentToken()
        validateTokens()

        return tokens
    }

    private inline fun ParsingContext.parseChar(char: Char) {
        while (true) {
            when (tryParseString(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseComment(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseSingleCharToken(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseStatement(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseNumber(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseWhitespace(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            when (tryParseIdentifier(char)) {
                ParsingResult.NEXT_CHAR -> return
                ParsingResult.NEXT_PARSER -> Unit
                ParsingResult.RESTART_PARSERS -> continue
            }

            return
        }
    }

    private inline fun ParsingContext.tryParseString(char: Char): ParsingResult {
        if (isComment) {
            return ParsingResult.NEXT_PARSER
        }

        if (char == EReservedChar.QUOTATION_MARK.value) {
            if (isCurrentTokenString()) {
                if (isStringEscape) {
                    currentTokenValue.append(EReservedChar.QUOTATION_MARK.value)

                    isStringEscape = false
                } else {
                    addCurrentToken()
                }
            } else {
                addCurrentToken()

                currentTokenType = EPrimitiveToken::class
                currentTokenSymbol = options.stringConstructor
            }

            return ParsingResult.NEXT_CHAR
        }

        if (isCurrentTokenString()) {
            if (char == EReservedChar.BACKSLASH.value) {
                if (isStringEscape) {
                    currentTokenValue.append(EReservedChar.BACKSLASH.value)

                    isStringEscape = false
                } else {
                    isStringEscape = true
                }
            } else if (char != CR) {
                if (isStringEscape) {
                    currentTokenValue.append(
                        when (char) {
                            't' -> '\t'
                            'r' -> CR
                            'n' -> LF
                            else -> throw EException(context, "unsupported char escape \\$char!")
                        },
                    )

                    isStringEscape = false
                } else {
                    currentTokenValue.append(char)
                }
            }

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseComment(char: Char): ParsingResult {
        if (isComment) {
            if (char == LF) {
                isComment = false

                return ParsingResult.NEXT_PARSER
            }

            return ParsingResult.NEXT_CHAR
        } else if (char == EReservedChar.NUMBER_SIGN.value) {
            addCurrentToken()

            isComment = true

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseSingleCharToken(char: Char): ParsingResult {
        if (char in SINGLE_CHAR_TOKENS_MAP) {
            addCurrentToken()

            val singleCharTokenType = SINGLE_CHAR_TOKENS_MAP[char]!!

            currentTokenType = singleCharTokenType

            if (singleCharTokenType == EOpenBracketToken::class) {
                bracketsCount++
            } else if (singleCharTokenType == ECloseBracketToken::class) {
                bracketsCount--

                if (bracketsCount < 0) {
                    throw EException(context, "wrong number of brackets!")
                }
            }

            addCurrentToken()

            if (singleCharTokenType == ESemicolonToken::class || singleCharTokenType == EOpenBracketToken::class) {
                isExpressionStart = true
            }

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseStatement(char: Char): ParsingResult {
        if (char == EReservedChar.LEFT_CURLY_BRACKET.value) {
            addCurrentToken()

            isExpressionStart = true

            curlyBracketsCount++

            tokensStack.addFirst(mutableListOf())
            bracketsCountStack.addFirst(0)

            return ParsingResult.NEXT_CHAR
        }

        if (char == EReservedChar.RIGHT_CURLY_BRACKET.value) {
            curlyBracketsCount--

            if (curlyBracketsCount < 0) {
                throw EException(context, "wrong number of curly brackets!")
            }

            addCurrentToken()
            validateTokens()

            currentTokenType = EPrimitiveToken::class
            currentTokenSymbol = options.statementConstructor

            bracketsCountStack.removeFirst()

            addCurrentToken()

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseNumber(char: Char): ParsingResult {
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
                if (currentTokenValue.isNotEmpty()) {
                    return ParsingResult.NEXT_PARSER
                }

                addCurrentToken()

                currentTokenType = EPrimitiveToken::class
                currentTokenValue.append(actualChar)
                currentTokenSymbol = options.numberConstructor
            }

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseWhitespace(char: Char): ParsingResult {
        if (char.isWhitespace()) {
            addCurrentToken()

            val isLastTokenValidForAutoSemicolon =
                tokens.isNotEmpty() &&
                    tokens[tokens.lastIndex] !is ESemicolonToken &&
                    tokens[tokens.lastIndex]::class in FINAL_TOKEN_TYPES

            if (char == LF &&
                isLastTokenValidForAutoSemicolon &&
                bracketsCount == 0
            ) {
                currentTokenType = SINGLE_CHAR_TOKENS_MAP[EReservedChar.SEMICOLON.value]!!

                addCurrentToken()

                isExpressionStart = true
            }

            return ParsingResult.NEXT_CHAR
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.tryParseIdentifier(char: Char): ParsingResult {
        if (currentTokenType != null && currentTokenType !in TOKEN_TYPES_WITH_IDENTIFIER) {
            addCurrentToken()
        }

        currentTokenValue.append(char)

        val findResult = findTokenTypeAndSymbol(currentTokenValue.toString())

        if (findResult != null) {
            currentTokenType = findResult.first
            currentTokenSymbol = findResult.second
        } else if (currentTokenType != null) {
            currentTokenValue.deleteAt(currentTokenValue.length - 1)

            addCurrentToken()

            return ParsingResult.RESTART_PARSERS
        }

        return ParsingResult.NEXT_PARSER
    }

    private inline fun ParsingContext.findTokenTypeAndSymbol(identifier: String): Pair<KClass<out EToken>, ESymbol>? {
        if (identifier == EReservedIdentifier.TRUE.value || identifier == EReservedIdentifier.FALSE.value) {
            return EPrimitiveToken::class to options.booleanConstructor
        }

        if (isExpressionStart) {
            leftUnaryOperatorsMap[identifier]?.let {
                return ELeftUnaryOperatorToken::class to it
            }
        } else {
            binaryOperatorsMap[identifier]?.let {
                return EBinaryOperatorToken::class to it
            }

            rightUnaryOperatorsMap[identifier]?.let {
                return ERightUnaryOperatorToken::class to it
            }
        }

        functionsMap[identifier]?.let {
            return EFunctionToken::class to it
        }

        return null
    }

    private inline fun ParsingContext.clearCurrentToken() {
        currentTokenType = null
        currentTokenValue.clear()
        currentTokenSymbol = null

        isNumberAfterPoint = false
    }

    private inline fun ParsingContext.addCurrentToken() {
        if (currentTokenType == null && currentTokenValue.isNotEmpty()) {
            throw EException(context, "unknown identifier $currentTokenValue!")
        }

        if (currentTokenType != null) {
            val currentToken =
                when (currentTokenType) {
                    ESemicolonToken::class -> ESemicolonToken

                    EOpenBracketToken::class -> EOpenBracketToken

                    ECloseBracketToken::class -> ECloseBracketToken

                    EPrimitiveToken::class ->
                        when (currentTokenSymbol) {
                            options.numberConstructor ->
                                {
                                    val value = currentTokenValue.toString()

                                    if (value == ".") {
                                        throw EException(context, "invalid number!")
                                    }

                                    EPrimitiveToken(
                                        value,
                                        currentTokenSymbol as ENumberConstructor,
                                    )
                                }

                            options.stringConstructor ->
                                EPrimitiveToken(
                                    currentTokenValue.toString(),
                                    currentTokenSymbol as EStringConstructor,
                                )

                            options.booleanConstructor ->
                                EPrimitiveToken(
                                    currentTokenValue.toString() == EReservedIdentifier.TRUE.value,
                                    currentTokenSymbol as EBooleanConstructor,
                                )

                            options.statementConstructor ->
                                EPrimitiveToken(
                                    tokensStack.removeFirst(),
                                    currentTokenSymbol as EStatementConstructor,
                                )

                            else -> throw EException(
                                context,
                                "unknown current token symbol ${if (currentTokenSymbol == null) {
                                    null
                                } else {
                                    currentTokenSymbol!!::class
                                }}!",
                            )
                        }

                    EBinaryOperatorToken::class -> EBinaryOperatorToken(currentTokenSymbol as EBinaryOperator)

                    ELeftUnaryOperatorToken::class -> ELeftUnaryOperatorToken(currentTokenSymbol as ELeftUnaryOperator)

                    ERightUnaryOperatorToken::class -> ERightUnaryOperatorToken(currentTokenSymbol as ERightUnaryOperator)

                    EFunctionToken::class -> EFunctionToken(currentTokenSymbol as EFunction)

                    else -> throw EException(context, "unknown current token type ${currentTokenType?.simpleName}!")
                }

            tokens.add(currentToken)

            clearCurrentToken()

            isExpressionStart = false

            validateLastTokenPosition()
        }
    }

    private inline fun ParsingContext.validateLastTokenPosition() {
        var (token1, token2) = getLastTwoTokens()

        if (token1 is ESemicolonToken && token2 is ECloseBracketToken) {
            tokens.removeAt(tokens.lastIndex - 1)

            val (newToken1, newToken2) = getLastTwoTokens()

            token1 = newToken1
            token2 = newToken2
        }

        val validPositions = TOKEN_POSITIONS_MAP[if (token1 == null) null else token1::class]!!

        if (token2::class !in validPositions) {
            throw EException(context, "position of $token2 is not valid!")
        }

        if (token1 is EOpenBracketToken && token2 is ECloseBracketToken) {
            if (tokens.size == 2) {
                throw EException(context, "brackets can't be empty!")
            }

            val token0 = tokens[tokens.lastIndex - 2]

            if (token0 is EFunctionToken) {
                tokens[tokens.lastIndex - 2] = token0.copy(withoutArguments = true)
            } else {
                throw EException(context, "brackets can't be empty!")
            }
        }
    }

    private inline fun ParsingContext.validateTokens() {
        if (bracketsCount != 0) {
            throw EException(context, "wrong number of brackets!")
        }

        if (tokens.isEmpty()) {
            throw EException(context, "expression can't be empty!")
        }

        if (tokens[tokens.lastIndex]::class !in FINAL_TOKEN_TYPES) {
            throw EException(context, "expression is not completed!")
        }

        if (tokens[tokens.lastIndex] is ESemicolonToken) {
            tokens.removeAt(tokens.lastIndex)
        }

        if (tokens.isEmpty()) {
            throw EException(context, "expression can't be empty!")
        }
    }

    private inline val ParsingContext.tokens
        get() = tokensStack.first()

    private inline var ParsingContext.bracketsCount
        get() = bracketsCountStack.first()
        set(value) {
            bracketsCountStack.set(0, value)
        }

    private inline fun ParsingContext.isCurrentTokenNumber(): Boolean =
        currentTokenType == EPrimitiveToken::class && currentTokenSymbol == options.numberConstructor

    private inline fun ParsingContext.isCurrentTokenString(): Boolean =
        currentTokenType == EPrimitiveToken::class && currentTokenSymbol == options.stringConstructor

    private inline fun ParsingContext.getLastTwoTokens(): Pair<EToken?, EToken> =
        if (tokens.size == 1) {
            null to tokens[0]
        } else {
            tokens[tokens.lastIndex - 1] to tokens[tokens.lastIndex]
        }
}
