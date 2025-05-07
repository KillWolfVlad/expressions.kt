package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import java.math.BigDecimal

class EBaseNumberClass : EClass {
    override val description = "base number class"

    override val identifier = "Number"

    override fun createInstance(arguments: List<Any>): EInstance {
        if (arguments.size != 1) {
            throw IllegalArgumentException("arguments must have only one parameter!")
        }

        val value =
            when (arguments[0]) {
                is String -> BigDecimal(arguments[0] as String)
                is EBaseNumberInstance -> BigDecimal((arguments[0] as EBaseNumberInstance).value.toString())
                is EBaseStringInstance -> BigDecimal((arguments[0] as EBaseStringInstance).value)
                else -> throw Exception("unknown argument type ${arguments[0]::class.simpleName}!")
            }

        return EBaseNumberInstance(value)
    }
}
