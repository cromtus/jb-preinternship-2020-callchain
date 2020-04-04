import io.StringInputBrowser
import parser.ModelError
import parser.parseModel
import applicator.applyModelTo


fun main() {
    val input = StringInputBrowser(readLine() ?: "")
    try {
        val model = parseModel(input)
        val newModel = model.transform()
        println(newModel)
        println(applyModelTo(model, listOf(-2,-1,0,1,2)))
        println(listOf(1,2,4))
    } catch (e: ModelError) {
        println(" ".repeat(input.caret) + "^")
        println(e.content)
        println(e.details)
    }
}