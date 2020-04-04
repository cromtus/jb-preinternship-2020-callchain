package model

import io.InputBrowser


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

    companion object {
        private const val CALLS_SEPARATOR = "%>%"

        fun parse(input: InputBrowser): CallChain {
            val callsList = ArrayList<Call>()
            while (true) {
                callsList.add(parseCall(input))
                if (!input.consume(CALLS_SEPARATOR)) {
                    return CallChain(callsList)
                }
            }
        }
    }
}