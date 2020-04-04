import io.StringInputBrowser
import model.Model
import model.ModelError


fun main() {
    val input = StringInputBrowser(readLine() ?: "")
    try {
        val model = Model.parse(input)
        val newModel = model.process()
        println(newModel)
    } catch (e: ModelError) {
        println(" ".repeat(input.caret) + "^")
        println(e.content)
        println(e.details)
    }
}