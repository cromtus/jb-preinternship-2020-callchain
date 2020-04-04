package model

import io.InputBrowser
import java.lang.StringBuilder


abstract class Expression {
    abstract val isBool: Boolean
    abstract fun substitute(expression: Expression): Expression
}

object Element : Expression() {
    override val isBool = false
    const val pattern = "element"

    override fun toString() = pattern

    override fun substitute(expression: Expression) = expression
}

enum class Operator {
    PLUS, MINUS, MULT, GREATER, LESS, EQUALS, AND, OR;

    val areOperandsBool get() = this in arrayOf(AND, OR)
    val isResultBool get() = this in arrayOf(GREATER, LESS, EQUALS, AND, OR)

    override fun toString() = when (this) {
        PLUS -> "+"
        MINUS -> "-"
        MULT -> "*"
        GREATER -> ">"
        LESS -> "<"
        EQUALS -> "="
        AND -> "&"
        OR -> "|"
    }

    companion object {
        fun parse(input: InputBrowser) = when {
            input.consume("+") -> PLUS
            input.consume("-") -> MINUS
            input.consume("*") -> MULT
            input.consume(">") -> GREATER
            input.consume("<") -> LESS
            input.consume("=") -> EQUALS
            input.consume("&") -> AND
            input.consume("|") -> OR
            else -> null
        }
    }
}

data class BinaryExpression(
        val leftOperand: Expression,
        val operator: Operator,
        val rightOperand: Expression
) : Expression() {
    override fun toString() = "$BEGIN$leftOperand$operator$rightOperand$END"
    override val isBool get() = operator.isResultBool

    override fun substitute(expression: Expression) = BinaryExpression(
            leftOperand.substitute(expression),
            operator,
            rightOperand.substitute(expression)
    )

    companion object {
        private const val BEGIN = "("
        private const val END = ")"

        fun parse(input: InputBrowser): BinaryExpression? {
            if (!input.consume(BEGIN)) return null
            val leftOperand = parseExpression(input) ?: return null

            val operator = Operator.parse(input) ?: return null

            val rightOperand = parseExpression(input) ?: return null
            if (!input.consume(END)) return null

            if (operator.areOperandsBool != leftOperand.isBool || operator.areOperandsBool != rightOperand.isBool) {
                throw TypeError()
            }
            return BinaryExpression(leftOperand, operator, rightOperand)
        }
    }
}

data class ConstExpression(val value: Long): Expression() {
    override val isBool = false

    override fun toString() = value.toString()

    override fun substitute(expression: Expression) = ConstExpression(value)

    companion object {
        fun parse(input: InputBrowser): ConstExpression? {
            val buf = StringBuilder()
            while (!input.end()) {
                val c = input.look()!!
                if (c.isDigit() || (c == '-' && buf.isEmpty())) {
                    buf.append(c)
                } else {
                    break
                }
                input.increment()
            }
            if (buf.isEmpty()) return null
            return ConstExpression(buf.toString().toLong())
        }
    }
}

fun parseExpression(input: InputBrowser): Expression? {
    if (input.consume(Element.pattern)) return Element
    BinaryExpression.parse(input)?.let { return it }
    return ConstExpression.parse(input)
}
