package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.base.binaryOperators.BaseEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseGreaterOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseLessOrEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BaseNotEqualBinaryOperator
import ru.killwolfvlad.expressions.base.binaryOperators.BasePlusBinaryOperator
import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentType
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Base string instance
 */
open class BaseStringInstance(
    override val value: String,
) : EInstance {
    companion object {
        private val context = BaseStringInstance::class.simpleName!!
    }

    override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance =
        baseValidateArgumentType<BaseStringInstance, EInstance>(context, other) {
            when (operator) {
                is BasePlusBinaryOperator -> BaseStringInstance(value + it.value)

                is BaseGreaterBinaryOperator -> BaseBooleanInstance(value > it.value)

                is BaseGreaterOrEqualBinaryOperator -> BaseBooleanInstance(value >= it.value)

                is BaseLessBinaryOperator -> BaseBooleanInstance(value < it.value)

                is BaseLessOrEqualBinaryOperator -> BaseBooleanInstance(value <= it.value)

                is BaseEqualBinaryOperator -> BaseBooleanInstance(value == it.value)

                is BaseNotEqualBinaryOperator -> BaseBooleanInstance(value != it.value)

                else -> throw EException(
                    context,
                    "unsupported binary operator type ${operator::class.simpleName}!",
                )
            }
        }

    override suspend fun applyLeftUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance =
        throw EException(
            context,
            "unsupported left unary operator type ${operator::class.simpleName}!",
        )

    override suspend fun applyRightUnaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance =
        throw EException(
            context,
            "unsupported right unary operator type ${operator::class.simpleName}!",
        )
}
