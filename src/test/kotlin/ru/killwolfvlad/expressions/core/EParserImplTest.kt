package ru.killwolfvlad.expressions.core

import io.kotest.core.spec.style.DescribeSpec
import ru.killwolfvlad.expressions.base.buildExpressionBaseOptions

class EParserImplTest : DescribeSpec({
    val parser = EParserImpl(buildExpressionBaseOptions())

    it("a") {
        val r =
            parser.parse(
                """
                "123" + ( "4456" - 8); String()
                """.trimIndent(),
            )

        r.forEach { println(it) }
    }

//
//    it("must parse semicolon") {
//        parser.parse(";") shouldBe listOf(EToken(ETokenType.SEMICOLON, ";"))
//    }
//
//    it("must parse open bracket") {
//        parser.parse("(") shouldBe listOf(EToken(ETokenType.OPEN_BRACKET, "("))
//    }
//
//    it("must parse close bracket") {
//        parser.parse(")") shouldBe listOf(EToken(ETokenType.CLOSE_BRACKET, ")"))
//    }
//
//    it("must parse digit") {
//        parser.parse("10") shouldBe listOf(EToken(ETokenType.NUMBER, "10"))
//    }
//
//    it("must parse digit with dot") {
//        parser.parse("10.25") shouldBe listOf(EToken(ETokenType.NUMBER, "10.25"))
//    }
//
//    it("must parse digit with comma") {
//        parser.parse("10,25") shouldBe listOf(EToken(ETokenType.NUMBER, "10.25"))
//    }
//
//    it("must split digits by space") {
//        parser.parse("10.25 67,38") shouldBe
//            listOf(
//                EToken(ETokenType.NUMBER, "10.25"),
//                EToken(ETokenType.NUMBER, "67.38"),
//            )
//    }
//
//    it("must parse single char word") {
//        parser.parse("+") shouldBe listOf(EToken(ETokenType.WORD, "+"))
//    }
//
//    it("must parse multi char word") {
//        parser.parse("add") shouldBe listOf(EToken(ETokenType.WORD, "add"))
//    }
//
//    it("must parse all types in one expression") {
//        parser.parse("+0$>2.30 * 3/ add\n( (- 56,1) ; pi )") shouldBe
//            listOf(
//                EToken(ETokenType.WORD, "+"),
//                EToken(ETokenType.NUMBER, "0"),
//                EToken(ETokenType.WORD, "$>"),
//                EToken(ETokenType.NUMBER, "2.30"),
//                EToken(ETokenType.WORD, "*"),
//                EToken(ETokenType.NUMBER, "3"),
//                EToken(ETokenType.WORD, "/"),
//                EToken(ETokenType.WORD, "add"),
//                EToken(ETokenType.OPEN_BRACKET, "("),
//                EToken(ETokenType.OPEN_BRACKET, "("),
//                EToken(ETokenType.WORD, "-"),
//                EToken(ETokenType.NUMBER, "56.1"),
//                EToken(ETokenType.CLOSE_BRACKET, ")"),
//                EToken(ETokenType.SEMICOLON, ";"),
//                EToken(ETokenType.WORD, "pi"),
//                EToken(ETokenType.CLOSE_BRACKET, ")"),
//            )
//    }
})
