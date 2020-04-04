package parser

import io.InputBrowser
import structures.Call
import structures.CallChain


internal const val CALLS_SEPARATOR = "%>%"

fun parseCallChain(input: InputBrowser): CallChain {
    val callsList = ArrayList<Call>()
    while (true) {
        callsList.add(parseCall(input))
        if (!input.consume(CALLS_SEPARATOR)) {
            return CallChain(callsList)
        }
    }
}