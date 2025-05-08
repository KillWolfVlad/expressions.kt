package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.types.EMemory

class EBaseStringClass : EClass {
    override val description = "base string class"

    override val identifier = "String"

    override suspend fun createInstance(
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance {
        if (arguments.size != 1) {
            throw IllegalArgumentException("arguments must have only one parameter!")
        }

        val value =
            when (arguments[0]) {
                is String -> arguments[0] as String
                is EBaseNumberInstance -> (arguments[0] as EBaseNumberInstance).value.toString()
                is EBaseStringInstance -> (arguments[0] as EBaseStringInstance).value
                else -> throw Exception("unknown argument type ${arguments[0]::class.simpleName}!")
            }

        return EBaseStringInstance(value)
    }
}
