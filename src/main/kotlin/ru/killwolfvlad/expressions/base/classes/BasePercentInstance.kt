package ru.killwolfvlad.expressions.base.classes

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Base percent instance
 */
open class BasePercentInstance(
    value: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode,
) : BaseNumberInstance(value, scale, roundingMode)
