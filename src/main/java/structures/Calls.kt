package structures

import parser.CALL_END
import parser.FILTER_CALL_BEGIN
import parser.MAP_CALL_BEGIN


sealed class Call

data class FilterCall(val expression: Expression) : Call() {
    override fun toString() = "$FILTER_CALL_BEGIN$expression$CALL_END"

    companion object {
        val IDENTITY = FilterCall(BinaryExpression(
            ConstExpression(1), Operator.EQUALS, ConstExpression(1)
        ))
    }
}

data class MapCall(val expression: Expression) : Call() {
    override fun toString() = "$MAP_CALL_BEGIN$expression$CALL_END"
}