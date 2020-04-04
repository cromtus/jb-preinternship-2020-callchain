package structures


sealed class Call

data class FilterCall(val expression: Expression) : Call() {
    companion object {
        val IDENTITY = FilterCall(BinaryExpression(
            ConstExpression(1), Operator.EQUALS, ConstExpression(1)
        ))
    }
}

data class MapCall(val expression: Expression) : Call()