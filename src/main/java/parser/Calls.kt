package parser

import io.InputBrowser
import structures.Call
import structures.Expression
import structures.FilterCall
import structures.MapCall


internal const val FILTER_CALL_BEGIN = "filter{"
internal const val MAP_CALL_BEGIN = "map{"
internal const val CALL_END = "}"

fun parseCall(input: InputBrowser): Call {
    when {
        input.consume(FILTER_CALL_BEGIN) -> FilterCall(
            parseExpression(input).let {
                if (it.type == Expression.Type.BOOLEAN) {
                    it
                } else {
                    throw TypeError("Filter argument must be boolean")
                }
            }
        )
        input.consume(MAP_CALL_BEGIN) -> MapCall(
            parseExpression(input).let {
                if (it.type == Expression.Type.NUMERIC) {
                    it
                } else {
                    throw TypeError("Map argument must be numeric")
                }
            }
        )
        else -> throw SyntaxError("Couldn't parse function call. You may have forgotten about '{'")
    }.let {
        if (input.consume(CALL_END)) {
            return it
        } else {
            throw SyntaxError("Missing $CALL_END")
        }
    }
}