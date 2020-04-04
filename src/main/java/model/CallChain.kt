package model

import io.ModelBrowser

data class CallChain(val calls: List<Call>) {
    override fun toString() = calls.joinToString(CALLS_SEPARATOR)

    fun process(): Model {
        var commonFilterCall: FilterCall? = null
        var currentExpression: Expression = Element
        for (call in calls) {
            when (call) {
                is MapCall -> {
                    currentExpression = call.expression.substitute(currentExpression)
                }
                is FilterCall -> {
                    val addition = call.expression.substitute(currentExpression)
                    if (commonFilterCall == null) {
                        commonFilterCall = FilterCall(addition)
                    } else {
                        commonFilterCall = FilterCall(BinaryExpression(
                                commonFilterCall.expression,
                                Operator.AND,
                                addition
                        ))
                    }
                }
            }
        }
        commonFilterCall = commonFilterCall ?: IDENTITY_FILTER_CALL
        return Model(CallChain(listOf(commonFilterCall, MapCall(currentExpression))))
    }

    companion object {
        private const val CALLS_SEPARATOR = "%>%"

        fun parse(input: ModelBrowser): CallChain? {
            val callsList = ArrayList<Call>()
            while (true) {
                callsList.add(parseCall(input) ?: return null)
                if (!input.consume(CALLS_SEPARATOR)) {
                    return CallChain(callsList)
                }
            }
        }
    }
}