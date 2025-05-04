package ru.killwolfvlad.expressions.core

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EClassInstance
import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.reflect.KClass

class PlusBinaryOperator : EBinaryOperator {
    override val name: String
        get() = "plus-binary-operator"

    override val order: Int
        get() = 2

    override val symbol: String
        get() = "+"
}

class MinusBinaryOperator : EBinaryOperator {
    override val name: String
        get() = "minus-binary-operator"

    override val order: Int
        get() = 2

    override val symbol: String
        get() = "-"
}

class MultiplyBinaryOperator : EBinaryOperator {
    override val name: String
        get() = "multiply-binary-operator"

    override val order: Int
        get() = 1

    override val symbol: String
        get() = "*"
}

class DivideBinaryOperator : EBinaryOperator {
    override val name: String
        get() = "divide-binary-operator"

    override val order: Int
        get() = 1

    override val symbol: String
        get() = "/"
}

class PlusLeftUnaryOperator : ELeftUnaryOperator {
    override val name: String
        get() = "plus-left-unary-operator"

    override val symbol: String
        get() = "+"
}

class MinusLeftUnaryOperator : ELeftUnaryOperator {
    override val name: String
        get() = "minus-left-unary-unary-operator"

    override val symbol: String
        get() = "-"
}

class FactorialRightUnaryOperator : ERightUnaryOperator {
    override val name: String
        get() = "factorial-right-unary-operator"

    override val symbol: String
        get() = "!"
}

class FindFunction(private val numberClass: EClass) : EFunction {
    override val name: String
        get() = "find-function"

    override val symbol: String
        get() = "find"

    override suspend fun execute(arguments: List<EClassInstance>): EClassInstance = numberClass.createInstance(listOf("3"))
}

class MoneyClass : EClass {
    override val name: String
        get() = "money"

    override val symbol: String
        get() = "Money"

    override fun createInstance(arguments: List<Any>): EClassInstance {
        if (arguments.size != 1) {
            throw Exception("wrong number of arguments!")
        }

        val value =
            if (arguments[0] is MoneyClassInstance) {
                (arguments[0] as MoneyClassInstance).value
            } else if (arguments[0] is BigDecimal) {
                arguments[0] as BigDecimal
            } else if (arguments[0] is String) {
                BigDecimal(arguments[0] as String)
            } else {
                throw Exception("unknown argument type!")
            }

        // see https://stackoverflow.com/a/1359905 for more info
        return MoneyClassInstance(
            value.setScale(2, RoundingMode.HALF_EVEN),
        )
    }
}

class MoneyClassInstance(override val value: BigDecimal) : EClassInstance {
    override val selfClass: KClass<EClass>
        get() = MoneyClass::class as KClass<EClass>

    override suspend fun applyBinaryOperator(
        other: EClassInstance,
        operator: EBinaryOperator,
    ): EClassInstance {
        if (other !is MoneyClassInstance) {
            throw Exception("wrong type!")
        }

        return when (operator) {
            is PlusBinaryOperator -> MoneyClassInstance(value + other.value)
            is MinusBinaryOperator -> MoneyClassInstance(value - other.value)
            is MultiplyBinaryOperator -> MoneyClassInstance(value * other.value)
            is DivideBinaryOperator -> MoneyClassInstance(value / other.value)
            else -> throw Exception("unsupported binary operator!")
        }
    }

    override suspend fun applyLeftUnaryOperator(operator: ELeftUnaryOperator): EClassInstance =
        when (operator) {
            is PlusLeftUnaryOperator -> MoneyClassInstance(value.plus())
            is MinusLeftUnaryOperator -> MoneyClassInstance(value.unaryMinus())
            else -> throw Exception("unsupported left unary operator!")
        }

    override suspend fun applyRightUnaryOperator(operator: ERightUnaryOperator): EClassInstance =
        when (operator) {
            is FactorialRightUnaryOperator -> MoneyClassInstance(value.plus(BigDecimal(25))) // TODO: factorial here!
            else -> throw Exception("unsupported right unary operator!")
        }
}

class ExpressionExecutorTest : DescribeSpec({
    val plusBinaryOperator = PlusBinaryOperator()
    val minusBinaryOperator = MinusBinaryOperator()
    val multiplyBinaryOperator = MultiplyBinaryOperator()
    val divideBinaryOperator = DivideBinaryOperator()

    val plusLeftUnaryOperator = PlusLeftUnaryOperator()
    val minusLeftUnaryOperator = MinusLeftUnaryOperator()

    val moneyClass = MoneyClass()

    val findFunction = FindFunction(moneyClass)

    val options =
        ExpressionExecutorOptions(
            binaryOperators = listOf(plusBinaryOperator, minusBinaryOperator, multiplyBinaryOperator, divideBinaryOperator),
            leftUnaryOperators = listOf(plusLeftUnaryOperator, minusLeftUnaryOperator),
            rightUnaryOperators = listOf(),
            classes = listOf(moneyClass),
            numberClass = moneyClass,
            functions = listOf(findFunction),
        )

    val expressionExecutor = ExpressionExecutor(options)

    it("1 + 2 = 3") {
        expressionExecutor.execute("1 + 2") shouldBe BigDecimal(3).setScale(2, RoundingMode.HALF_EVEN)
    }
})
