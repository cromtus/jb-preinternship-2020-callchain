package model

import io.ExpressionBrowser

class Expression {
    override fun toString() = "expr"

    companion object {
        fun parse(input: ExpressionBrowser) : Expression? {
            return Expression()
        }
    }
}