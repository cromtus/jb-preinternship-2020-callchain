import io.InputBrowser
import model.Model


fun main() {
    val input = InputBrowser(readLine() ?: "")
    val model = Model.parse(input)
    val newModel = model.process()
    print(newModel)
}