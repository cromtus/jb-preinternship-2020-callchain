package model

import io.ModelBrowser
import java.lang.Exception

data class Model(val root: CallChain) {
    override fun toString(): String {
        return root.toString()
    }

    fun process() = root.process()

    companion object {
        fun parse(input: ModelBrowser): Model {
            val callChain = CallChain.parse(input) ?: throw Exception("SYNTAX ERROR")
            if (!input.end()) throw Exception("SYNTAX ERROR")
            return Model(callChain)
        }
    }
}