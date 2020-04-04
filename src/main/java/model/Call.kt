package model

import io.InputBrowser


sealed class Call {
    companion object {
        const val END = "}"
    }
}

data class FilterCall(val expression: Expression) : Call() {
    override fun toString() = "$BEGIN$expression$END"

    companion object {
        const val BEGIN = "filter{"
        val IDENTITY = FilterCall(BinaryExpression(
            ConstExpression(1), Operator.EQUALS, ConstExpression(1)
        ))
    }
}

data class MapCall(val expression: Expression) : Call() {
    override fun toString() = "$BEGIN$expression$END"

    companion object {
        const val BEGIN = "map{"
    }
}

fun parseCall(input: InputBrowser): Call {
    val call = when {
        input.consume(FilterCall.BEGIN) -> FilterCall(
            parseExpression(input).let {
                if (it.type == Expression.Type.BOOLEAN) {
                    it
                } else {
                    throw TypeError("Filter argument must be boolean")
                }
            }
        )
        input.consume(MapCall.BEGIN) -> MapCall(
            parseExpression(input).let {
                if (it.type == Expression.Type.NUMERIC) {
                    it
                } else {
                    throw TypeError("Map argument must be numeric")
                }
            }
        )
        else -> throw SyntaxError("Couldn't parse function call. You may have forgotten about '{'")
    }
    if (input.consume(Call.END)) {
        return call
    } else {
        throw SyntaxError("Missing ${Call.END}")
    }
}