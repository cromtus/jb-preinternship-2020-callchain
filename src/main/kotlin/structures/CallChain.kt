package structures

import parser.CALLS_SEPARATOR


data class CallChain(val calls: Iterable<Call>) {
    override fun toString() = calls.joinToString(CALLS_SEPARATOR)

    fun transform(): Model {
        var commonFilterCall: FilterCall? = null
        var currentExpression: Expression = Element
        for (call in calls) {
            when (call) {
                is MapCall -> {
                    currentExpression = call.expression.substitute(currentExpression)
                }
                is FilterCall -> {
                    val addition = call.expression.substitute(currentExpression)
                    commonFilterCall = if (commonFilterCall == null) {
                        FilterCall(addition)
                    } else {
                        FilterCall(BinaryExpression(
                            commonFilterCall.expression,
                            Operator.AND,
                            addition
                        ))
                    }
                }
            }
        }
        commonFilterCall = commonFilterCall ?: FilterCall.IDENTITY
        return Model(CallChain(listOf(commonFilterCall, MapCall(currentExpression))))
    }
}