package ru.killwolfvlad.expressions.base.primitives

import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator
import ru.killwolfvlad.expressions.core.tokens.EToken

/**
 * Base statement instance
 */
open class BaseStatementInstance(
    override val value: List<EToken>,
) : EInstance {
    companion object {
        private val context = BaseStatementInstance::class.simpleName!!
    }

    override suspend fun applyBinaryOperator(
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        other: EInstance,
        operator: EBinaryOperator,
    ): EInstance =
        throw EException(
            context,
            "unsupported binary operator type ${operator::class.simpleName}!",
        )

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
