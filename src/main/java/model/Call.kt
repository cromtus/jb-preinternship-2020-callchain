package model

import io.InputBrowser


sealed class Call {
    companion object {
        const val END = "}"
    }
}

data class FilterCall(val expression: Expression): Call() {
    override fun toString() = "$BEGIN$expression$END"

    companion object {
        const val BEGIN = "filter{"
    }
}

val IDENTITY_FILTER_CALL = FilterCall(BinaryExpression(Element, Operator.EQUALS, Element))

data class MapCall(val expression: Expression): Call() {
    override fun toString() = "$BEGIN$expression$END"

    companion object {
        const val BEGIN = "map{"
    }
}

fun parseCall(input: InputBrowser): Call? {
    val call = when {
        input.consume(FilterCall.BEGIN) -> FilterCall(
                (parseExpression(input) ?: return null).let {
                    if (it.isBool) it else throw TypeError()
                }
        )
        input.consume(MapCall.BEGIN) -> MapCall(
                (parseExpression(input) ?: return null).let {
                    if (!it.isBool) it else throw TypeError()
                }
        )
        else -> return null
    }
    return if (input.consume(Call.END)) call else null
}