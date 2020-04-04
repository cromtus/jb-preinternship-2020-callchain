import io.StringInputBrowser
import parser.ModelError
import parser.parseModel


fun main() {
    val input = StringInputBrowser(readLine() ?: "")
    try {
        val model = parseModel(input)
        val newModel = model.transform()
        println(newModel)
    } catch (e: ModelError) {
        println(" ".repeat(input.caret) + "^")
        println(e.content)
        println(e.details)
    }
}