package model

import io.InputBrowser
import java.lang.Exception


data class Model(val root: CallChain) {
    override fun toString(): String {
        return root.toString()
    }

    fun process() = root.process()

    companion object {
        fun parse(input: InputBrowser): Model {
            val callChain = CallChain.parse(input) ?: throw Exception("SYNTAX ERROR")
            if (!input.end()) throw Exception("SYNTAX ERROR")
            return Model(callChain)
        }
    }
}