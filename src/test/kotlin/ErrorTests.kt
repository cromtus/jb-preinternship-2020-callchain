import io.StringInputBrowser
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import parser.SyntaxError
import parser.TypeError
import parser.parseModel

class ErrorTests {
    fun getModel(s: String) = parseModel(StringInputBrowser(s))

    @Test
    fun emptyInput() {
        assertThrows<SyntaxError> { getModel("") }
    }

    @Test
    fun strangeInput() {
        assertThrows<SyntaxError> { getModel("меня ондатра покусала") }
    }

    @Test
    fun incompleteChain() {
        assertThrows<SyntaxError> { getModel("map{2}%>%") }
    }

    @Test
    fun unexpectedSequence() {
        assertThrows<SyntaxError> { getModel("map{((element*element)<a)}") }
    }

    @Test
    fun typeErrorInCall() {
        assertThrows<TypeError> { getModel("filter{2}") }
        assertThrows<TypeError> { getModel("map{(1=1)}") }
    }

    @Test
    fun typeErrorInExpression() {
        assertThrows<TypeError> { getModel("map{((element<3)+1)}") }
    }
}