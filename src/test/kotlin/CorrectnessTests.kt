import applicator.applyModelTo
import io.StringInputBrowser
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

import parser.parseModel


class CorrectnessTests {
    fun makeTest(
        initialArray: Iterable<Int>,
        stringCallChain: String,
        kotlinCallChain: (Iterable<Int>) -> Iterable<Int>
    ) {
        val model = parseModel(StringInputBrowser(stringCallChain))
        val mutatedModel = model.transform()

        val kotlinResult = kotlinCallChain(initialArray)
        val modelResult = applyModelTo(model, initialArray)
        val mutatedModelResult = applyModelTo(mutatedModel, initialArray)

        assertEquals(kotlinResult.toString(), modelResult.toString())
        assertEquals(kotlinResult.toString(), mutatedModelResult.toString())
    }

    @Test
    fun simple() = makeTest(
        1..3,
        "map{2}"
    ) {
        it.map { 2 }
    }

    @Test
    fun emptyResult() = makeTest(
        1..3,
        "filter{(0=1)}"
    ) {
        it.filter { false }
    }

    @Test
    fun filterBetweenMaps() = makeTest(
        6..14,
        "map{(element-10)}%>%filter{(element>0)}%>%map{(element+10)}"
    ) {
        it.map { it - 10 }.filter { it > 0 }.map { it + 10 }
    }

    @Test
    fun allOperators() = makeTest(
        -100..100,
        listOf(
            "filter{(element<0)}",
            "map{(element+100)}",
            "map{((element-30)*(70-element))}",
            "filter{((element>0)|(element=-2100))}",
            "filter{((2=2)&(1=1))}"
        ).joinToString("%>%")
    ) {
        it.filter { it < 0 }.map { it + 100 }.map { (it - 30) * (70 - it) }
            .filter { it > 0 || it == -2100 }
    }
}