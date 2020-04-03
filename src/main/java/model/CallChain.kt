package model

import io.ExpressionBrowser

class CallChain constructor(private val calls: List<Call>) {
    override fun toString() = calls.joinToString()

    companion object {
        private const val CALL_DELIMITER = "%>%"

        fun parse(input: ExpressionBrowser): CallChain? {
            val callsList = ArrayList<Call>()
            while (true) {
                val call = FilterCall.parse(input) ?: MapCall.parse(input) ?: return null
                callsList.add(call)
                if (!input.consume(CALL_DELIMITER)) {
                    return CallChain(callsList)
                }
            }
        }
    }
}