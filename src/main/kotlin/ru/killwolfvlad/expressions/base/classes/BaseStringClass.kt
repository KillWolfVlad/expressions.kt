package ru.killwolfvlad.expressions.base.classes

import ru.killwolfvlad.expressions.base.validators.baseValidateArgumentsCount
import ru.killwolfvlad.expressions.core.exceptions.EException
import ru.killwolfvlad.expressions.core.interfaces.EClass
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.types.EMemory

/**
 * Base string class
 */
open class BaseStringClass : EClass {
    override val description = "base string class"

    override val identifier = "String"

    override suspend fun createInstance(
        memory: EMemory,
        arguments: List<Any>,
    ): EInstance =
        baseValidateArgumentsCount(identifier, arguments, setOf(1)) {
            val argument = arguments[0]

            val value =
                when (argument) {
                    is String -> argument

                    is EInstance -> argument.value.toString()

                    else -> throw EException(
                        identifier,
                        "unsupported argument type ${argument::class.simpleName}!",
                    )
                }

            return BaseStringInstance(value)
        }
}
