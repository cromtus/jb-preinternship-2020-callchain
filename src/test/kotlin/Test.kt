import applicator.applyModelTo
import org.junit.Assert.assertEquals
import org.junit.Test
import io.StringInputBrowser
import parser.parseModel


class Test {
    @Test fun simple() {
        val array = listOf<Long>(1, 2, 3)
        val model = parseModel(StringInputBrowser("map{2}"))
        val newModel = model.transform()

        val result1 = array.map { 2 }
        val result2 = applyModelTo(model, array)
        val result3 = applyModelTo(newModel, array)
        println("array1: $result1")
        println("array2: $result2")
        println("array3: $result3")
        assertEquals(result1.toString(), result2.toString())
    }
}