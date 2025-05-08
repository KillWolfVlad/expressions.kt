package ru.killwolfvlad.expressions.base

import ru.killwolfvlad.expressions.core.interfaces.EFunction
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.types.EMemory

class EBaseVarFunction : EFunction {
    override val description = "store and get variables"

    override val identifier = "var"

    override suspend fun execute(
        memory: EMemory,
        arguments: List<EInstance>,
    ): EInstance {
        if (arguments.size != 1 && arguments.size != 2) {
            throw Exception("$identifier: wrong arguments count!")
        }

        val key = (arguments[0] as? EBaseStringInstance ?: throw Exception("var: key must be string!")).value

        if (arguments.size == 1) {
            return memory.kv[key] as EInstance
                ?: throw Exception("variable with name $key is not defined!")
        } else if (arguments.size == 2) {
            memory.kv[key] = arguments[1]

            return EBaseNumberInstance.ZERO
        }

        throw Exception("$identifier: wrong arguments count!")
    }
}
