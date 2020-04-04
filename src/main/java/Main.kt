import io.ModelBrowser
import model.Model

fun main() {
    val input = ModelBrowser(readLine() ?: "")
    val model = Model.parse(input)
    val newModel = model.process()
    print(newModel)
}