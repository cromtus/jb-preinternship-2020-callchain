import io.ExpressionBrowser
import model.Model

fun main() {
    val input = ExpressionBrowser(readLine() ?: "")
    val model = Model.parse(input)
    print(model)
}