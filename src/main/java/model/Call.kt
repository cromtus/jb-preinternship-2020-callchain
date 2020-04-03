package model

import io.ExpressionBrowser


abstract class Call

abstract class CallFactory {
    abstract val BEGIN: String
    private val END = "}"

    abstract fun handleExpression(input: ExpressionBrowser) : Call?

    open fun parse(input: ExpressionBrowser) : Call? {
        if (!input.consume(BEGIN)) return null
        val call = handleExpression(input) ?: return null
        if (!input.consume(END)) return null
        return call
    }
}

class FilterCall constructor(private val expression: Expression): Call() {
    override fun toString() = "fil($expression)"

    companion object: CallFactory() {
        override val BEGIN = "filter{"

        override fun handleExpression(input: ExpressionBrowser): FilterCall? {
            val expr = Expression.parse(input) ?: return null
            return FilterCall(expr)
        }

        override fun parse(input: ExpressionBrowser) : FilterCall? {
            return super.parse(input) as FilterCall?
        }
    }
}

class MapCall constructor(private val expression: Expression): Call() {
    override fun toString() = "map($expression)"

    companion object: CallFactory() {
        override val BEGIN = "map{"

        override fun handleExpression(input: ExpressionBrowser): MapCall? {
            val expr = Expression.parse(input) ?: return null
            return MapCall(expr)
        }

        override fun parse(input: ExpressionBrowser): MapCall? {
            return super.parse(input) as MapCall?
        }
    }
}