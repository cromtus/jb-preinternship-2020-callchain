package io


class InputBrowser(private val expression: String) {
    private var caret = 0

    fun consume(pattern: String): Boolean {
        val end = caret + pattern.length
        if (end <= expression.length && expression.substring(caret, end) == pattern) {
            caret += pattern.length
            return true
        }
        return false
    }

    fun look() = if (!end()) expression[caret] else null

    fun increment() = ++caret

    fun end() = expression.length == caret
}