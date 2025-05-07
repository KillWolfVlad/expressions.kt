package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import java.math.BigDecimal
import kotlin.plus

class EBaseNumberInstance(
    override val value: BigDecimal,
) : EInstance {
    override suspend fun applyBinaryOperator(
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance {
        if (other !is EBaseNumberInstance) {
            throw Exception("can't apply binary operator for different type ${other::class.simpleName}!")
        }

        return when (operator) {
            is EBasePlusBinaryOperator -> EBaseNumberInstance(value + other.value)
            is EBaseMinusBinaryOperator -> EBaseNumberInstance(value - other.value)
            is EBaseMultiplyBinaryOperator -> EBaseNumberInstance(value * other.value)
            is EBaseDivideBinaryOperator -> EBaseNumberInstance(value / other.value)
            is EBaseExponentiationBinaryOperator -> EBaseNumberInstance(value.pow(other.value.toInt()))
            else -> throw Exception("unknown binary operator ${operator::class.simpleName}!")
        }
    }

    override suspend fun applyLeftUnaryOperator(operator: ELeftUnaryOperator): EInstance =
        when (operator) {
            is EBasePlusLeftUnaryOperator -> EBaseNumberInstance(value.plus())
            is EBaseMinusLeftUnaryOperator -> EBaseNumberInstance(value.negate())
            else -> throw Exception("unknown left unary operator ${operator::class.simpleName}!")
        }

    override suspend fun applyRightUnaryOperator(operator: ERightUnaryOperator): EInstance =
        throw Exception("unknown right unary operator ${operator::class.simpleName}!")
}
