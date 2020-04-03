package model

import io.ExpressionBrowser
import java.lang.Exception

class Model private constructor(private val root: CallChain) {
    override fun toString(): String {
        return root.toString()
    }

    companion object {
        fun parse(input: ExpressionBrowser): Model {
            val callChain = CallChain.parse(input) ?: throw Exception("SYNTAX ERROR")
            if (!input.end()) throw Exception("SYNTAX ERROR")
            return Model(callChain)
        }
    }
}