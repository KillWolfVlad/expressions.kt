package ru.killwolfvlad.expressions.base.memory

import ru.killwolfvlad.expressions.base.interfaces.BaseBinaryOperatorApplier
import ru.killwolfvlad.expressions.base.interfaces.BaseLeftUnaryOperatorApplier
import ru.killwolfvlad.expressions.base.interfaces.BaseRightUnaryOperatorApplier
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EBinaryOperator
import ru.killwolfvlad.expressions.core.symbols.ELeftUnaryOperator
import ru.killwolfvlad.expressions.core.symbols.ERightUnaryOperator

/**
 * Base applier
 */
open class BaseApplier(
    baseBinaryOperatorAppliers: List<BaseBinaryOperatorApplier>,
    baseLeftUnaryOperatorAppliers: List<BaseLeftUnaryOperatorApplier>,
    baseRightUnaryOperatorAppliers: List<BaseRightUnaryOperatorApplier>,
) {
    // TODO: add validation to duplications!

    protected val binaryOperatorsMap =
        baseBinaryOperatorAppliers.flatMap { it.types.map { typePair -> Triple(it.operator, typePair, it) } }
            .groupBy { it.first }
            .mapValues { (_, v) -> v.groupBy({ it.second.first }, { it.second.second to it.third }) }
            .mapValues { (_, v1) -> v1.mapValues { (_, v2) -> v2.toMap() } }

    protected val leftUnaryOperatorAppliersMap =
        baseLeftUnaryOperatorAppliers.flatMap { it.types.map { type -> Triple(it.operator, type, it) } }
            .groupBy { it.first }
            .mapValues { (_, v) -> v.associateBy({ it.second }, { it.third }) }

    protected val rightUnaryOperatorAppliersMap =
        baseRightUnaryOperatorAppliers.flatMap { it.types.map { type -> Triple(it.operator, type, it) } }
            .groupBy { it.first }
            .mapValues { (_, v) -> v.associateBy({ it.second }, { it.third }) }

    /**
     * Apply binary operator
     */
    suspend fun applyBinaryOperator(
        value: EInstance,
        other: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: EBinaryOperator,
    ): EInstance {
        val typesMap = binaryOperatorsMap[operator::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have any implementations!",
        )

        val typeMap = typesMap[value::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have implementation for first type ${value::class.simpleName}!",
        )

        val applier = typeMap[other::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have implementation for second type ${other::class.simpleName}!",
        )

        return applier.apply(value, other, expressionExecutor, memory)
    }

    /**
     * Apply left unary operator
     */
    suspend fun applyLeftUnaryOperator(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ELeftUnaryOperator,
    ): EInstance {
        val typeMap = leftUnaryOperatorAppliersMap[operator::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have any implementations!",
        )

        val applier = typeMap[value::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have implementation for type ${value::class.simpleName}!",
        )

        return applier.apply(value, expressionExecutor, memory)
    }

    /**
     * Apply right unary operator
     */
    suspend fun applyRightUnaryOperator(
        value: EInstance,
        expressionExecutor: ExpressionExecutor,
        memory: EMemory,
        operator: ERightUnaryOperator,
    ): EInstance {
        val typeMap = rightUnaryOperatorAppliersMap[operator::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have any implementations!",
        )

        val applier = typeMap[value::class] ?: throw EException(
            operator::class.simpleName!!,
            "don't have implementation for type ${value::class.simpleName}!",
        )

        return applier.apply(value, expressionExecutor, memory)
    }
}
