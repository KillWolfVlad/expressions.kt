package ru.killwolfvlad.expressions.core

class A

//
// import io.kotest.core.spec.style.DescribeSpec
// import io.kotest.matchers.shouldBe
// import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
// import ru.killwolfvlad.expressions.core.interfaces.EClass
// import ru.killwolfvlad.expressions.core.interfaces.EFunction
// import ru.killwolfvlad.expressions.core.interfaces.EInstance
// import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
// import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
// import ru.killwolfvlad.expressions.core.types.EOptions
// import java.math.BigDecimal
// import java.math.RoundingMode
//
// class FactorialRightUnaryOperator : ERightUnaryOperator {
//    override val description: String
//        get() = "factorial-right-unary-operator"
//
//    override val identifier: String
//        get() = "!"
// }
//
// class FindFunction(private val numberClass: EClass) : EFunction {
//    override val description: String
//        get() = "find-function"
//
//    override val identifier: String
//        get() = "find"
//
//    override suspend fun execute(arguments: List<EInstance>): EInstance = numberClass.createInstance(listOf("3"))
// }
//
// class MoneyClass : EClass {
//    override val description: String
//        get() = "money"
//
//    override val identifier: String
//        get() = "Money"
//
//    override suspend fun createInstance(arguments: List<Any>): EInstance {
//        if (arguments.size != 1) {
//            throw Exception("wrong number of arguments!")
//        }
//
//        val value =
//            if (arguments[0] is MoneyInstance) {
//                (arguments[0] as MoneyInstance).value
//            } else if (arguments[0] is BigDecimal) {
//                arguments[0] as BigDecimal
//            } else if (arguments[0] is String) {
//                BigDecimal(arguments[0] as String)
//            } else {
//                throw Exception("unknown argument type!")
//            }
//
//        // see https://stackoverflow.com/a/1359905 for more info
//        return MoneyInstance(
//            this,
//            value.setScale(2, RoundingMode.HALF_EVEN),
//        )
//    }
// }
//
// class MoneyInstance(override val selfClass: EClass, override val value: BigDecimal) : EInstance {
//    override suspend fun applyBinaryOperator(
//        other: EInstance,
//        operator: EBinaryOperator,
//    ): EInstance {
//        if (other !is MoneyInstance) {
//            throw Exception("wrong type!")
//        }
//
//        return when (operator) {
//            is PlusBinaryOperator -> MoneyInstance(selfClass, value + other.value)
//            is MinusBinaryOperator -> MoneyInstance(selfClass, value - other.value)
//            is MultiplyBinaryOperator -> MoneyInstance(selfClass, value * other.value)
//            is DivideBinaryOperator -> MoneyInstance(selfClass, value / other.value)
//            else -> throw Exception("unsupported binary operator!")
//        }
//    }
//
//    override suspend fun applyLeftUnaryOperator(operator: ELeftUnaryOperator): EInstance =
//        when (operator) {
//            is PlusLeftUnaryOperator -> MoneyInstance(selfClass, value.plus())
//            is MinusLeftUnaryOperator -> MoneyInstance(selfClass, value.unaryMinus())
//            else -> throw Exception("unsupported left unary operator!")
//        }
//
//    override suspend fun applyRightUnaryOperator(operator: ERightUnaryOperator): EInstance =
//        when (operator) {
//            is FactorialRightUnaryOperator -> MoneyInstance(selfClass, value.plus(BigDecimal(25))) // TODO: factorial here!
//            else -> throw Exception("unsupported right unary operator!")
//        }
// }
//
// class ExpressionExecutorTest : DescribeSpec({
//    val plusBinaryOperator = PlusBinaryOperator()
//    val minusBinaryOperator = MinusBinaryOperator()
//    val multiplyBinaryOperator = MultiplyBinaryOperator()
//    val divideBinaryOperator = DivideBinaryOperator()
//
//    val plusLeftUnaryOperator = PlusLeftUnaryOperator()
//    val minusLeftUnaryOperator = MinusLeftUnaryOperator()
//
//    val moneyClass = MoneyClass()
//
//    val findFunction = FindFunction(moneyClass)
//
//    val options =
//        EOptions(
//            binaryOperators = listOf(plusBinaryOperator, minusBinaryOperator, multiplyBinaryOperator, divideBinaryOperator),
//            leftUnaryOperators = listOf(plusLeftUnaryOperator, minusLeftUnaryOperator),
//            rightUnaryOperators = listOf(),
//            classes = listOf(moneyClass),
//            functions = listOf(findFunction),
//            numberClass = moneyClass,
//        )
//
//    val expressionExecutor = ExpressionExecutor(options)
//
//    it("1 + 2 = 3") {
//        expressionExecutor.execute("1 + 2") shouldBe BigDecimal(3).setScale(2, RoundingMode.HALF_EVEN)
//    }
// })
