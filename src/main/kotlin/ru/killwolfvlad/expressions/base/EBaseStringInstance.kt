package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EBinaryOperator
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.interfaces.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.types.EMemory

class EBaseStringInstance(
    override val value: String,
) : EInstance {
    override suspend fun applyBinaryOperator(
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance {
        if (other !is EBaseStringInstance) {
            throw Exception("can't apply binary operator for different type ${other::class.simpleName}!")
        }

        return when (operator) {
            is EBasePlusBinaryOperator -> EBaseStringInstance(value + other.value)
            else -> throw Exception("unknown binary operator ${operator::class.simpleName}!")
        }
    }

    override suspend fun applyLeftUnaryOperator(
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance = throw Exception("unknown left unary operator ${operator::class.simpleName}!")

    override suspend fun applyRightUnaryOperator(
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance = throw Exception("unknown right unary operator ${operator::class.simpleName}!")
}
