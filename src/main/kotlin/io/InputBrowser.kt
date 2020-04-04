package io


abstract class InputBrowser {
    abstract fun consume(length: Int, filter: (String) -> Boolean): String?
    abstract fun look(): Char?
    abstract fun increment()
    abstract fun end(): Boolean

    fun consume(pattern: String): Boolean = consume(pattern.length) {
        it == pattern
    } != null
}

class StringInputBrowser(private val expression: String) : InputBrowser() {
    var caret = 0
        private set

    override fun consume(length: Int, filter: (String) -> Boolean): String? {
        val end = caret + length
        if (end <= expression.length) {
            val substring = expression.substring(caret, end)
            if (filter(substring)) {
                caret += length
                return substring
            }
        }
        return null
    }

    override fun look() = if (!end()) expression[caret] else null

    override fun increment() {
        ++caret
    }

    override fun end() = expression.length <= caret
}