package ru.killwolfvlad.expressions.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.enums.EAssociativity
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.EFunction
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.tokens.EBinaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ECloseBracketToken
import ru.killwolfvlad.expressions.core.tokens.EFunctionToken
import ru.killwolfvlad.expressions.core.tokens.ELeftUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.EOpenBracketToken
import ru.killwolfvlad.expressions.core.tokens.EPrimitiveToken
import ru.killwolfvlad.expressions.core.tokens.ERightUnaryOperatorToken
import ru.killwolfvlad.expressions.core.tokens.ESemicolonToken
import ru.killwolfvlad.expressions.core.tokens.EToken

private data class WrongIdentifierTestCase(
    val name: String,
    val identifier: String,
    val exceptionMessage: String,
)

class ExpressionParserTest : DescribeSpec({
    val defaultOptions = buildBaseExpressionOptions()
    val expressionParser = ExpressionParser(defaultOptions)

    fun getNumberToken(
        value: String,
        options: EOptions = defaultOptions,
    ): EPrimitiveToken<String> = EPrimitiveToken(value, options.numberConstructor)

    fun getStringToken(
        value: String,
        options: EOptions = defaultOptions,
    ): EPrimitiveToken<String> = EPrimitiveToken(value, options.stringConstructor)

    fun getBooleanToken(
        value: Boolean,
        options: EOptions = defaultOptions,
    ): EPrimitiveToken<Boolean> = EPrimitiveToken(value, options.booleanConstructor)

    fun getStatementToken(
        value: List<EToken>,
        options: EOptions = defaultOptions,
    ): EPrimitiveToken<List<EToken>> = EPrimitiveToken(value, options.statementConstructor)

    fun getBinaryOperatorToken(
        value: String,
        options: EOptions = defaultOptions,
    ): EBinaryOperatorToken = EBinaryOperatorToken(options.binaryOperators.find { it.identifier == value }!!)

    fun getLeftUnaryOperatorToken(
        value: String,
        options: EOptions = defaultOptions,
    ): ELeftUnaryOperatorToken = ELeftUnaryOperatorToken(options.leftUnaryOperators.find { it.identifier == value }!!)

    fun getRightUnaryOperatorToken(
        value: String,
        options: EOptions = defaultOptions,
    ): ERightUnaryOperatorToken = ERightUnaryOperatorToken(options.rightUnaryOperators.find { it.identifier == value }!!)

    fun getFunctionToken(
        value: String,
        withoutArguments: Boolean = false,
        options: EOptions = defaultOptions,
    ): EFunctionToken = EFunctionToken(options.functions.find { it.identifier == value }!!, withoutArguments)

    describe("init") {
        it("must throw exception if left unary operators has duplicates") {
            val options = defaultOptions.copy(leftUnaryOperators = defaultOptions.leftUnaryOperators + defaultOptions.leftUnaryOperators)

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier +!"
        }

        it("must throw exception if functions has duplicates") {
            val options = defaultOptions.copy(functions = defaultOptions.functions + defaultOptions.functions)

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier Number!"
        }

        it("must throw exception if binary operators has duplicates") {
            val options = defaultOptions.copy(binaryOperators = defaultOptions.binaryOperators + defaultOptions.binaryOperators)

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier +!"
        }

        it("must throw exception if right unary operators has duplicates") {
            val options = defaultOptions.copy(rightUnaryOperators = defaultOptions.rightUnaryOperators + defaultOptions.rightUnaryOperators)

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier %!"
        }

        it("must throw exception if left unary operators and functions has duplicates") {
            val options =
                defaultOptions.copy(
                    leftUnaryOperators =
                        listOf(
                            object : ELeftUnaryOperator {
                                override val identifier: String = "fun"
                            },
                        ),
                )

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier fun!"
        }

        it("must throw exception if binary operators and right unary operators has duplicates") {
            val options =
                defaultOptions.copy(
                    binaryOperators =
                        listOf(
                            object : EBinaryOperator {
                                override val identifier: String = "%"

                                override val priority = 0

                                override val associativity = EAssociativity.LR
                            },
                        ),
                )

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier %!"
        }

        it("must throw exception if right unary operators and functions has duplicates") {
            val options =
                defaultOptions.copy(
                    rightUnaryOperators =
                        listOf(
                            object : ERightUnaryOperator {
                                override val identifier: String = "fun"
                            },
                        ),
                )

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier fun!"
        }

        it("must throw exception if binary operators and functions has duplicates") {
            val options =
                defaultOptions.copy(
                    binaryOperators =
                        listOf(
                            object : EBinaryOperator {
                                override val identifier: String = "fun"

                                override val priority = 0

                                override val associativity = EAssociativity.LR
                            },
                        ),
                )

            shouldThrowExactly<EException> {
                ExpressionParser(options)
            }.message shouldBe "ExpressionParser: found duplicated identifier fun!"
        }

        val wrongIdentifierTestCases =
            listOf(
                WrongIdentifierTestCase(
                    name = "must throw exception if identifier is empty",
                    identifier = "",
                    exceptionMessage = "ExpressionParser: identifier can't be empty!",
                ),
                WrongIdentifierTestCase(
                    name = "must throw exception if identifier is reserved",
                    identifier = "true",
                    exceptionMessage = "ExpressionParser: identifier true is reserved!",
                ),
                WrongIdentifierTestCase(
                    name = "must throw exception if identifier starts from digit",
                    identifier = "2B",
                    exceptionMessage = "ExpressionParser: identifier 2B can't start from digit!",
                ),
                WrongIdentifierTestCase(
                    name = "must throw exception if identifier contains reserved char",
                    identifier = "a#b",
                    exceptionMessage = "ExpressionParser: identifier a#b can't contains forbidden char!",
                ),
                WrongIdentifierTestCase(
                    name = "must throw exception if identifier contains whitespace",
                    identifier = "a b",
                    exceptionMessage = "ExpressionParser: identifier a b can't contains forbidden char!",
                ),
            )

        describe("must validate binary operator identifier") {
            wrongIdentifierTestCases.forEach { testCase ->
                it(testCase.name) {
                    shouldThrowExactly<EException> {
                        ExpressionParser(
                            defaultOptions.copy(
                                binaryOperators =
                                    listOf(
                                        object : EBinaryOperator {
                                            override val identifier = testCase.identifier

                                            override val priority = 0

                                            override val associativity = EAssociativity.LR
                                        },
                                    ),
                            ),
                        )
                    }.message shouldBe testCase.exceptionMessage
                }
            }
        }

        describe("must validate left unary operator identifier") {
            wrongIdentifierTestCases.forEach { testCase ->
                it(testCase.name) {
                    shouldThrowExactly<EException> {
                        ExpressionParser(
                            defaultOptions.copy(
                                leftUnaryOperators =
                                    listOf(
                                        object : ELeftUnaryOperator {
                                            override val identifier = testCase.identifier
                                        },
                                    ),
                            ),
                        )
                    }.message shouldBe testCase.exceptionMessage
                }
            }
        }

        describe("must validate right unary operator identifier") {
            wrongIdentifierTestCases.forEach { testCase ->
                it(testCase.name) {
                    shouldThrowExactly<EException> {
                        ExpressionParser(
                            defaultOptions.copy(
                                rightUnaryOperators =
                                    listOf(
                                        object : ERightUnaryOperator {
                                            override val identifier = testCase.identifier
                                        },
                                    ),
                            ),
                        )
                    }.message shouldBe testCase.exceptionMessage
                }
            }
        }

        describe("must validate functions identifier") {
            wrongIdentifierTestCases.forEach { testCase ->
                it(testCase.name) {
                    shouldThrowExactly<EException> {
                        ExpressionParser(
                            defaultOptions.copy(
                                functions =
                                    listOf(
                                        object : EFunction {
                                            override val identifier = testCase.identifier

                                            override suspend fun execute(
                                                expressionExecutor: ExpressionExecutor,
                                                memory: EMemory,
                                                arguments: List<EInstance>,
                                            ): EInstance = throw NotImplementedError()
                                        },
                                    ),
                            ),
                        )
                    }.message shouldBe testCase.exceptionMessage
                }
            }
        }
    }

    describe("parse string") {
        it("must parse empty string") {
            expressionParser.parse("\"\"") shouldBe listOf(getStringToken(""))
        }

        it("must parse string from one digit") {
            expressionParser.parse("\"1\"") shouldBe listOf(getStringToken("1"))
        }

        it("must parse string from one whitespace") {
            expressionParser.parse("\" \"") shouldBe listOf(getStringToken(" "))
        }

        it("must parse string from one alpha") {
            expressionParser.parse("\"a\"") shouldBe listOf(getStringToken("a"))
        }

        it("must parse multi line string") {
            expressionParser.parse(
                """
                " ab
                  cd"
                """.trimIndent(),
            ) shouldBe listOf(getStringToken(" ab\n  cd"))
        }

        describe("must escape chars") {
            listOf(
                "\\\"" to '\"',
                "\\t" to '\t',
                "\\r" to '\r',
                "\\n" to '\n',
                "\\\\" to '\\',
            ).forEach { testCase ->
                it("must escape ${testCase.first}") {
                    expressionParser.parse("\"a${testCase.first}b\"") shouldBe listOf(getStringToken("a${testCase.second}b"))
                }
            }

            it("must throw exception if char escape is unsupported") {
                shouldThrowExactly<EException> {
                    expressionParser.parse("\"\\b\"")
                }.message shouldBe "ExpressionParser: unsupported char escape \\b!"
            }
        }

        it("must ignore CR char") {
            expressionParser.parse("\"a\r\nb\"") shouldBe listOf(getStringToken("a\nb"))
        }

        it("must throw exception if string without closing quotation mark") {
            shouldThrowExactly<EException> {
                expressionParser.parse("\"a")
            }.message shouldBe "ExpressionParser: string without closing quotation mark!"
        }

        it("must don't parse string inside comments") {
            expressionParser.parse(
                """
                # "percent / 100"
                1000
                """.trimIndent(),
            ) shouldBe listOf(getNumberToken("1000"))
        }
    }

    describe("parse comment") {
        it("must parse comments") {
            expressionParser.parse(
                """
                # comment 1
                "a#b" + "cb" # comment 2
                 # comment 3
                 10 + 20 # comment 4
                  # comment 5
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getStringToken("a#b"),
                    getBinaryOperatorToken("+"),
                    getStringToken("cb"),
                    ESemicolonToken,
                    getNumberToken("10"),
                    getBinaryOperatorToken("+"),
                    getNumberToken("20"),
                )
        }
    }

    describe("parse single char tokens") {
        it("must parse semicolon") {
            expressionParser.parse("20;30") shouldBe
                listOf(
                    getNumberToken("20"),
                    ESemicolonToken,
                    getNumberToken("30"),
                )
        }

        it("must remove trailing semicolon") {
            expressionParser.parse("20;") shouldBe
                listOf(
                    getNumberToken("20"),
                )
        }

        it("must parse brackets") {
            expressionParser.parse("(20)") shouldBe
                listOf(
                    EOpenBracketToken,
                    getNumberToken("20"),
                    ECloseBracketToken,
                )
        }

        it("must throw exception if expression starts from close bracket") {
            shouldThrowExactly<EException> {
                expressionParser.parse(")")
            }.message shouldBe "ExpressionParser: wrong number of brackets!"
        }

        it("must throw exception if expression don't has closing bracket") {
            shouldThrowExactly<EException> {
                expressionParser.parse("(20")
            }.message shouldBe "ExpressionParser: wrong number of brackets!"
        }
    }

    describe("parse statement") {
        it("must parse statement") {
            expressionParser.parse(
                """
                {
                  {
                    30
                  }
                }
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getStatementToken(
                        listOf(
                            getStatementToken(
                                listOf(
                                    getNumberToken("30"),
                                ),
                            ),
                        ),
                    ),
                )
        }

        it("must throw exception if expression starts from right curly bracket") {
            shouldThrowExactly<EException> {
                expressionParser.parse("}")
            }.message shouldBe "ExpressionParser: wrong number of curly brackets!"
        }

        it("must throw exception if statement is not closed") {
            shouldThrowExactly<EException> {
                expressionParser.parse("{10")
            }.message shouldBe "ExpressionParser: wrong number of curly brackets!"
        }
    }

    describe("parse number") {
        it("must parse number from single digit") {
            expressionParser.parse("3") shouldBe listOf(getNumberToken("3"))
        }

        it("must parse number from multiple digits") {
            expressionParser.parse("25") shouldBe listOf(getNumberToken("25"))
        }

        it("must parse number with point") {
            expressionParser.parse("25.67") shouldBe listOf(getNumberToken("25.67"))
        }

        it("must parse number with comma") {
            expressionParser.parse("25,67") shouldBe listOf(getNumberToken("25.67"))
        }

        it("must throw exception if number contains two points") {
            shouldThrowExactly<EException> {
                expressionParser.parse("3.25.6")
            }.message shouldBe "ExpressionParser: invalid number with double points!"
        }

        it("must throw exception if number only from one point") {
            shouldThrowExactly<EException> {
                expressionParser.parse(".")
            }.message shouldBe "ExpressionParser: invalid number!"
        }

        it("must skip number parsing when digits is part of identifier") {
            val options =
                defaultOptions.copy(
                    functions =
                        listOf(
                            object : EFunction {
                                override val identifier = "pow2"

                                override suspend fun execute(
                                    expressionExecutor: ExpressionExecutor,
                                    memory: EMemory,
                                    arguments: List<EInstance>,
                                ): EInstance = throw NotImplementedError()
                            },
                        ),
                )

            val parser = ExpressionParser(options)

            parser.parse("pow2(3)") shouldBe
                listOf(
                    getFunctionToken("pow2", options = options),
                    EOpenBracketToken,
                    getNumberToken("3"),
                    ECloseBracketToken,
                )
        }
    }

    describe("parse whitespace") {
        it("must insert semicolon after new line") {
            expressionParser.parse(
                """
                30
                25
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getNumberToken("30"),
                    ESemicolonToken,
                    getNumberToken("25"),
                )
        }

        it("must insert semicolon after new line inside statement") {
            expressionParser.parse(
                """
                {
                  30
                  25
                }
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getStatementToken(
                        listOf(
                            getNumberToken("30"),
                            ESemicolonToken,
                            getNumberToken("25"),
                        ),
                    ),
                )
        }

        it("must don't insert semicolon if semicolon already exists") {
            expressionParser.parse(
                """
                30;
                25
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getNumberToken("30"),
                    ESemicolonToken,
                    getNumberToken("25"),
                )
        }
    }

    describe("parse identifier") {
        it("must parse true") {
            expressionParser.parse("true") shouldBe listOf(getBooleanToken(true))
        }

        it("must parse false") {
            expressionParser.parse("false") shouldBe listOf(getBooleanToken(false))
        }

        it("must parse left unary operator after expression start") {
            expressionParser.parse("+10") shouldBe
                listOf(
                    getLeftUnaryOperatorToken("+"),
                    getNumberToken("10"),
                )
        }

        it("must parse left unary operator after semicolon") {
            expressionParser.parse("30;+10") shouldBe
                listOf(
                    getNumberToken("30"),
                    ESemicolonToken,
                    getLeftUnaryOperatorToken("+"),
                    getNumberToken("10"),
                )
        }

        it("must parse left unary operator after left curly bracket") {
            expressionParser.parse("{+10}") shouldBe
                listOf(
                    getStatementToken(
                        listOf(
                            getLeftUnaryOperatorToken("+"),
                            getNumberToken("10"),
                        ),
                    ),
                )
        }

        it("must parse left unary operator after new line") {
            expressionParser.parse(
                """
                10
                -20
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getNumberToken("10"),
                    ESemicolonToken,
                    getLeftUnaryOperatorToken("-"),
                    getNumberToken("20"),
                )
        }

        it("must parse binary operator") {
            expressionParser.parse("10*50") shouldBe
                listOf(
                    getNumberToken("10"),
                    getBinaryOperatorToken("*"),
                    getNumberToken("50"),
                )
        }

        it("must parse binary operator greedily") {
            expressionParser.parse("10**50") shouldBe
                listOf(
                    getNumberToken("10"),
                    getBinaryOperatorToken("**"),
                    getNumberToken("50"),
                )
        }

        it("must parse right unary operator") {
            expressionParser.parse("10%") shouldBe
                listOf(
                    getNumberToken("10"),
                    getRightUnaryOperatorToken("%"),
                )
        }

        it("must parse function") {
            expressionParser.parse(
                """
                var("a"; 10)
                """.trimIndent(),
            ) shouldBe
                listOf(
                    getFunctionToken("var"),
                    EOpenBracketToken,
                    getStringToken("a"),
                    ESemicolonToken,
                    getNumberToken("10"),
                    ECloseBracketToken,
                )
        }

        it("must throw exception if identifier is unknown") {
            shouldThrowExactly<EException> {
                expressionParser.parse("a")
            }.message shouldBe "ExpressionParser: unknown identifier a!"
        }
    }

    describe("validate last token position") {
        describe("when one token") {
            it("must throw exception if token position is not valid") {
                shouldThrowExactly<EException> {
                    expressionParser.parse(";")
                }.message shouldBe "ExpressionParser: position of ; is not valid!"
            }
        }

        describe("when two tokens") {
            it("must remove trailing semicolon before close bracket") {
                expressionParser.parse(
                    """
                    var(
                      1;
                      2;
                    )
                    """.trimIndent(),
                ) shouldBe
                    listOf(
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getNumberToken("1"),
                        ESemicolonToken,
                        getNumberToken("2"),
                        ECloseBracketToken,
                    )
            }

            it("must throw exception if token position is not valid") {
                shouldThrowExactly<EException> {
                    expressionParser.parse("10fun")
                }.message shouldBe "ExpressionParser: position of fun(...) is not valid!"
            }

            it("must throw exception if brackets empty") {
                shouldThrowExactly<EException> {
                    expressionParser.parse("()")
                }.message shouldBe "ExpressionParser: brackets can't be empty!"
            }
        }

        describe("when three or more tokens") {
            it("if brackets empty after function must set withoutArguments = true") {
                expressionParser.parse("fun()") shouldBe
                    listOf(
                        getFunctionToken("fun", true),
                        EOpenBracketToken,
                        ECloseBracketToken,
                    )
            }

            it("must throw exception if brackets empty") {
                shouldThrowExactly<EException> {
                    expressionParser.parse("-()")
                }.message shouldBe "ExpressionParser: brackets can't be empty!"
            }
        }
    }

    describe("validate tokens") {
        it("must throw exception if expression empty") {
            shouldThrowExactly<EException> {
                expressionParser.parse("")
            }.message shouldBe "ExpressionParser: expression can't be empty!"
        }

        it("must throw exception if expression is not completed") {
            shouldThrowExactly<EException> {
                expressionParser.parse("10+")
            }.message shouldBe "ExpressionParser: expression is not completed!"
        }
    }

    it("example") {
        val options =
            defaultOptions.copy(
                functions =
                    defaultOptions.functions +
                        listOf(
                            object : EFunction {
                                override val identifier = "someFun"

                                override suspend fun execute(
                                    expressionExecutor: ExpressionExecutor,
                                    memory: EMemory,
                                    arguments: List<EInstance>,
                                ): EInstance = throw NotImplementedError()
                            },
                        ),
            )

        val parser = ExpressionParser(options)

        parser.parse(
            """
            # comments starts from #

            10 # this is number, also everything after # is comment and ignored
            10.25 # number can be with point
            10,25 # you also can use comma instead of point

            "this is string" # simple string
            "\" or \\ or \t or \r or \n" # you can escape some chars

            "string also can be multiline
            \r symbols always ignored in string"

            true # use this reserved identifier for boolean true
            false # use this reserved identifier for boolean false
            # be careful, because parsing is case sensitivity

            -10 # plus left unary operator
            +10 # minus left unary operator
            !true # not left unary operator
            # left unary operator can be only one
            # left unary operator applies after right unary operator

            10 + 20 # plus binary operator
            10 - 20 # minus binary operator
            10 / 20 # divide binary operator
            10 * 20 # multiply binary operator
            2 ** 3 # exponential binary operator, has right to left association, so 2 ** 3 ** 2 = 512
            true && false # and binary operator
            true || false # or binary operator
            10 > 20 # greater binary operator
            10 >= 20 # greater or qual binary operator
            10 < 20 # less binary operator
            10 <= 20 # less or qual binary operator
            10 == 20 # qual binary operator
            10 != 20 # not qual binary operator
            # binary operator applies after left unary operator
            # binary operator priority - 1) exponentiation (**)
            #                            2) multiply (*, /)
            #                            3) plus (+, -)
            #                            4) compare (>, >=, <, <=)
            #                            5) equal (==, !=)
            #                            6) and (&&)
            #                            7) or (||)

            10% # percent right unary operator
            # right unary operator can be only one
            # right unary operator has highest priority

            1000 + 20% # 1200, you can easily add percentages
            1000 - 20% # 800, you can easily subtract percentages

            # in other cases percent will be used as "percent / 100"
            1000 * 20% # 200
            1000 / 20% # 500

            -(10 + 20) # you can also use brackets
            +(3 * (2 + 3)) # and brackets inside brackets

            someFun() # invoke some built-in function without arguments (this function doesn't exists in base)
            someFun(10; 20; 30) # invoke some built-in function with arguments (use ; to separate arguments)

            10; 20; 30 # you can also ; to separate sub-expressions, last sub-expression will be used as result of execution
            # ; is not required in most cases - you can just use new line and ; will be inserted automatically

            10 +
            20 # if expression is not completed auto ; is disabled

            someFun(10;
                    20;
                    30; # trailing ; also supported
            ) # inside brackets auto ; is disabled

            # built-in function - if
            if(true; 1; 2) # first argument - condition, second argument - then, third argument - else

            # built-in function - var
            # you can set variables
            var("a"; 2)
            var("b"; 3)

            # and read it later
            var("a") + var("b")

            var("a"; 5) # you also can reassign variable, but it is not recommended, better create new variable
            var("a"; "different type") # you can't reassign variable with different type

            # built-in function - fun
            # you can defined function
            fun("sum"; "a"; "b"; { # place expression body inside {}
              # inside function you can also get access to other functions and variables in current visible scope
              var("a") + var("b")
            })

            fun("sum"; 2; 3) # and invoke it later

            # you can also define function inside function
            fun("double and pow 2"; "a"; {
              fun("double"; "a"; {
                var("a") * 2
              })

              var("a"; fun("double"; var("a")))

              fun("pow2"; "a"; {
                var("a") ** 2
              })

              fun("pow2"; var("a"))
            })

            fun("double and pow 2"; 5) # you can use any string as function identifier, because it is string :)
            # but for built-in function you can't use any identifier

            # language is dynamically typed, but strong typed under the under the hood
            # you can't work with different types
            10 + "2" # it is error

            # you must use functions to explicit convert types
            Number("10") # to convert string or boolean to number
            String(10) # to convert number or boolean to string
            Boolean(1) # to convert number or string to boolean
            """.trimIndent(),
        ) shouldBe
            listOf(
                getNumberToken("10"),
                ESemicolonToken,
                getNumberToken("10.25"),
                ESemicolonToken,
                getNumberToken("10.25"),
                ESemicolonToken,
                //
                getStringToken("this is string"),
                ESemicolonToken,
                getStringToken("\" or \\ or \t or \r or \n"),
                ESemicolonToken,
                //
                getStringToken("string also can be multiline\n\r symbols always ignored in string"),
                ESemicolonToken,
                //
                getBooleanToken(true),
                ESemicolonToken,
                getBooleanToken(false),
                ESemicolonToken,
                //
                getLeftUnaryOperatorToken("-"),
                getNumberToken("10"),
                ESemicolonToken,
                getLeftUnaryOperatorToken("+"),
                getNumberToken("10"),
                ESemicolonToken,
                getLeftUnaryOperatorToken("!"),
                getBooleanToken(true),
                ESemicolonToken,
                //
                getNumberToken("10"),
                getBinaryOperatorToken("+"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("-"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("/"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("*"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("2"),
                getBinaryOperatorToken("**"),
                getNumberToken("3"),
                ESemicolonToken,
                getBooleanToken(true),
                getBinaryOperatorToken("&&"),
                getBooleanToken(false),
                ESemicolonToken,
                getBooleanToken(true),
                getBinaryOperatorToken("||"),
                getBooleanToken(false),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken(">"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken(">="),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("<"),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("<="),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("=="),
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("10"),
                getBinaryOperatorToken("!="),
                getNumberToken("20"),
                ESemicolonToken,
                //
                getNumberToken("10"),
                getRightUnaryOperatorToken("%"),
                ESemicolonToken,
                //
                getNumberToken("1000"),
                getBinaryOperatorToken("+"),
                getNumberToken("20"),
                getRightUnaryOperatorToken("%"),
                ESemicolonToken,
                getNumberToken("1000"),
                getBinaryOperatorToken("-"),
                getNumberToken("20"),
                getRightUnaryOperatorToken("%"),
                ESemicolonToken,
                //
                getNumberToken("1000"),
                getBinaryOperatorToken("*"),
                getNumberToken("20"),
                getRightUnaryOperatorToken("%"),
                ESemicolonToken,
                getNumberToken("1000"),
                getBinaryOperatorToken("/"),
                getNumberToken("20"),
                getRightUnaryOperatorToken("%"),
                ESemicolonToken,
                //
                getLeftUnaryOperatorToken("-"),
                EOpenBracketToken,
                getNumberToken("10"),
                getBinaryOperatorToken("+"),
                getNumberToken("20"),
                ECloseBracketToken,
                ESemicolonToken,
                getLeftUnaryOperatorToken("+"),
                EOpenBracketToken,
                getNumberToken("3"),
                getBinaryOperatorToken("*"),
                EOpenBracketToken,
                getNumberToken("2"),
                getBinaryOperatorToken("+"),
                getNumberToken("3"),
                ECloseBracketToken,
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("someFun", true, options),
                EOpenBracketToken,
                ECloseBracketToken,
                ESemicolonToken,
                getFunctionToken("someFun", options = options),
                EOpenBracketToken,
                getNumberToken("10"),
                ESemicolonToken,
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("30"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getNumberToken("10"),
                ESemicolonToken,
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("30"),
                ESemicolonToken,
                //
                getNumberToken("10"),
                getBinaryOperatorToken("+"),
                getNumberToken("20"),
                ESemicolonToken,
                //
                getFunctionToken("someFun", options = options),
                EOpenBracketToken,
                getNumberToken("10"),
                ESemicolonToken,
                getNumberToken("20"),
                ESemicolonToken,
                getNumberToken("30"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("if"),
                EOpenBracketToken,
                getBooleanToken(true),
                ESemicolonToken,
                getNumberToken("1"),
                ESemicolonToken,
                getNumberToken("2"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("a"),
                ESemicolonToken,
                getNumberToken("2"),
                ECloseBracketToken,
                ESemicolonToken,
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("b"),
                ESemicolonToken,
                getNumberToken("3"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("a"),
                ECloseBracketToken,
                getBinaryOperatorToken("+"),
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("b"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("a"),
                ESemicolonToken,
                getNumberToken("5"),
                ECloseBracketToken,
                ESemicolonToken,
                getFunctionToken("var"),
                EOpenBracketToken,
                getStringToken("a"),
                ESemicolonToken,
                getStringToken("different type"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("fun"),
                EOpenBracketToken,
                getStringToken("sum"),
                ESemicolonToken,
                getStringToken("a"),
                ESemicolonToken,
                getStringToken("b"),
                ESemicolonToken,
                getStatementToken(
                    listOf(
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getStringToken("a"),
                        ECloseBracketToken,
                        getBinaryOperatorToken("+"),
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getStringToken("b"),
                        ECloseBracketToken,
                    ),
                ),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("fun"),
                EOpenBracketToken,
                getStringToken("sum"),
                ESemicolonToken,
                getNumberToken("2"),
                ESemicolonToken,
                getNumberToken("3"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("fun"),
                EOpenBracketToken,
                getStringToken("double and pow 2"),
                ESemicolonToken,
                getStringToken("a"),
                ESemicolonToken,
                getStatementToken(
                    listOf(
                        getFunctionToken("fun"),
                        EOpenBracketToken,
                        getStringToken("double"),
                        ESemicolonToken,
                        getStringToken("a"),
                        ESemicolonToken,
                        getStatementToken(
                            listOf(
                                getFunctionToken("var"),
                                EOpenBracketToken,
                                getStringToken("a"),
                                ECloseBracketToken,
                                getBinaryOperatorToken("*"),
                                getNumberToken("2"),
                            ),
                        ),
                        ECloseBracketToken,
                        ESemicolonToken,
                        //
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getStringToken("a"),
                        ESemicolonToken,
                        getFunctionToken("fun"),
                        EOpenBracketToken,
                        getStringToken("double"),
                        ESemicolonToken,
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getStringToken("a"),
                        ECloseBracketToken,
                        ECloseBracketToken,
                        ECloseBracketToken,
                        ESemicolonToken,
                        //
                        getFunctionToken("fun"),
                        EOpenBracketToken,
                        getStringToken("pow2"),
                        ESemicolonToken,
                        getStringToken("a"),
                        ESemicolonToken,
                        getStatementToken(
                            listOf(
                                getFunctionToken("var"),
                                EOpenBracketToken,
                                getStringToken("a"),
                                ECloseBracketToken,
                                getBinaryOperatorToken("**"),
                                getNumberToken("2"),
                            ),
                        ),
                        ECloseBracketToken,
                        ESemicolonToken,
                        //
                        getFunctionToken("fun"),
                        EOpenBracketToken,
                        getStringToken("pow2"),
                        ESemicolonToken,
                        getFunctionToken("var"),
                        EOpenBracketToken,
                        getStringToken("a"),
                        ECloseBracketToken,
                        ECloseBracketToken,
                    ),
                ),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getFunctionToken("fun"),
                EOpenBracketToken,
                getStringToken("double and pow 2"),
                ESemicolonToken,
                getNumberToken("5"),
                ECloseBracketToken,
                ESemicolonToken,
                //
                getNumberToken("10"),
                getBinaryOperatorToken("+"),
                getStringToken("2"),
                ESemicolonToken,
                //
                getFunctionToken("Number"),
                EOpenBracketToken,
                getStringToken("10"),
                ECloseBracketToken,
                ESemicolonToken,
                getFunctionToken("String"),
                EOpenBracketToken,
                getNumberToken("10"),
                ECloseBracketToken,
                ESemicolonToken,
                getFunctionToken("Boolean"),
                EOpenBracketToken,
                getNumberToken("1"),
                ECloseBracketToken,
            )
    }
})
