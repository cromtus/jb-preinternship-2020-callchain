package model

import io.InputBrowser


data class Model(val root: CallChain) {
    override fun toString(): String {
        return root.toString()
    }

    fun process() = root.process()

    companion object {
        fun parse(input: InputBrowser): Model {
            val callChain = CallChain.parse(input)
            if (!input.end()) throw SyntaxError("Root expression must be only a call-chain")
            return Model(callChain)
        }
    }
}