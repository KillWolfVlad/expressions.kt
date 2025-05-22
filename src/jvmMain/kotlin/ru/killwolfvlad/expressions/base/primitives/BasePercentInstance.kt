package ru.killwolfvlad.expressions.base.primitives

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Base percent instance
 */
actual open class BasePercentInstance(
    value: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode,
) : BaseNumberInstance(value, scale, roundingMode)
