import io.StringInputBrowser
import org.junit.Test
import parser.parseModel
import org.junit.jupiter.api.Assertions.assertEquals

class BasicTests {
    @Test
    fun simplyWorks() {
        val model = parseModel(StringInputBrowser(
            "map{(element*2)}%>%filter{(element>1)}"))
        val mutatedModel = model.transform()
        assertEquals(
            "filter{((element*2)>1)}%>%map{(element*2)}",
            mutatedModel.toString()
        )
    }
}