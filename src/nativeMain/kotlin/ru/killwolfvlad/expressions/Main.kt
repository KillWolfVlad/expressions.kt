package ru.killwolfvlad.expressions

import kotlinx.coroutines.runBlocking
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.base.primitives.BaseStringInstance
import ru.killwolfvlad.expressions.core.ExpressionExecutor
import ru.killwolfvlad.expressions.core.interfaces.EInstance
import ru.killwolfvlad.expressions.core.interfaces.EMemory
import ru.killwolfvlad.expressions.core.symbols.EFunction
import kotlin.system.exitProcess

fun main() {
    try {
        runBlocking {
            runRepl()
        }
    } catch (exception: Exception) {
        println(exception.message)
        exitProcess(1)
    }
}

suspend fun runRepl() {
    var expression = ""
    var isClear = false
    var isExit = false

    val options =
        buildBaseExpressionOptions().let { defaultOptions ->
            defaultOptions.copy(
                functions =
                    defaultOptions.functions +
                        listOf(
                            object : EFunction {
                                override val identifier = "exit"

                                override suspend fun execute(
                                    expressionExecutor: ExpressionExecutor,
                                    memory: EMemory,
                                    arguments: List<EInstance>,
                                ): EInstance {
                                    isExit = true

                                    return BaseStringInstance("")
                                }
                            },
                            object : EFunction {
                                override val identifier = "clear"

                                override suspend fun execute(
                                    expressionExecutor: ExpressionExecutor,
                                    memory: EMemory,
                                    arguments: List<EInstance>,
                                ): EInstance {
                                    isClear = true

                                    return BaseStringInstance("")
                                }
                            },
                        ),
            )
        }

    val expressionExecutor = ExpressionExecutor(options)

    println("expressions.kt REPL")
    println("input \"exit()\" to exit from REPL")
    println("input \"clear()\" to clear REPL")
    println()

    while (true) {
        print("> ")
        val tempExpression = expression + "\n" + readln()

        try {
            val result = expressionExecutor.execute(tempExpression).value

            if (isExit) {
                break
            }

            println(result)

            if (isClear) {
                expression = ""
                isClear = false
            } else {
                expression = tempExpression
            }
        } catch (exception: Exception) {
            println(exception.message)
        }
    }
}
