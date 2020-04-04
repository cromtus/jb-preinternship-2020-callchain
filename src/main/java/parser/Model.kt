package parser

import io.InputBrowser
import structures.Model


fun parseModel(input: InputBrowser): Model {
    val callChain = parseCallChain(input)
    if (!input.end()) throw SyntaxError("Root expression can be nothing more but a call-chain")
    return Model(callChain)
}