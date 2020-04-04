package parser

import java.lang.Exception


open class ModelError(val content: String, val details: String) : Exception("$content\n$details")

class TypeError constructor(details: String) : ModelError("TYPE ERROR", details)

class SyntaxError constructor(details: String) : ModelError("SYNTAX ERROR", details)