package ru.killwolfvlad.expressions

import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.HTMLTextAreaElement
import ru.killwolfvlad.expressions.base.buildBaseExpressionOptions
import ru.killwolfvlad.expressions.core.ExpressionExecutor

val expressionExecutor = ExpressionExecutor(buildBaseExpressionOptions())

suspend fun onExpressionChange(expression: String) {
    var result: Any
    var isError = false

    try {
        result = expressionExecutor.execute(expression).value
    } catch (error: Exception) {
        result = error.message ?: "Error"
        isError = true
    }

    (document.getElementById("answer") as HTMLParagraphElement).innerHTML =
        """
        ${if (isError) "Error" else "Answer"}: <span>$result</span>
        """.trimIndent()
}

@JsExport
fun main() {
    (document.getElementById("expression") as HTMLTextAreaElement).addEventListener("input", { event ->
        GlobalScope.promise {
            onExpressionChange((event.target as HTMLTextAreaElement).value)
        }
    })

    GlobalScope.promise {
        onExpressionChange("")
    }
}
