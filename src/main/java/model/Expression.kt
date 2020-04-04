package model

import io.InputBrowser
import java.lang.StringBuilder


sealed class Expression {
    abstract val type: Type
    abstract fun substitute(expression: Expression): Expression

    enum class Type {
        BOOLEAN, NUMERIC
    }
}

object Element : Expression() {
    override val type = Type.NUMERIC
    const val stringValue = "element"

    override fun toString() = stringValue

    override fun substitute(expression: Expression) = expression
}

enum class Operator(
    val operandsType: Expression.Type,
    val resultType: Expression.Type,
    val stringValue: String
) {
    PLUS(Expression.Type.NUMERIC, Expression.Type.NUMERIC, "+"),
    MINUS(Expression.Type.NUMERIC, Expression.Type.NUMERIC, "-"),
    MULT(Expression.Type.NUMERIC, Expression.Type.NUMERIC, "*"),
    GREATER(Expression.Type.NUMERIC, Expression.Type.BOOLEAN, ">"),
    LESS(Expression.Type.NUMERIC, Expression.Type.BOOLEAN, "<"),
    EQUALS(Expression.Type.NUMERIC, Expression.Type.BOOLEAN, "="),
    AND(Expression.Type.BOOLEAN, Expression.Type.BOOLEAN, "&"),
    OR(Expression.Type.BOOLEAN, Expression.Type.BOOLEAN, "|");

    override fun toString() = stringValue

    companion object {
        fun parse(input: InputBrowser): Operator? {
            for (operator in Operator.values()) {
                if (input.consume(operator.stringValue)) {
                    return operator
                }
            }
            return null
        }
    }
}

data class BinaryExpression(
    val leftOperand: Expression,
    val operator: Operator,
    val rightOperand: Expression
) : Expression() {
    override fun toString() = "$BEGIN$leftOperand$operator$rightOperand$END"
    override val type get() = operator.resultType

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
            val leftOperand = parseExpression(input)

            val operator = Operator.parse(input) ?:
                throw SyntaxError("Unknown operator")

            val rightOperand = parseExpression(input)
            if (!input.consume(END)) throw SyntaxError("Missing $END")

            if (operator.operandsType != leftOperand.type || operator.operandsType != rightOperand.type) {
                throw TypeError("Inconsistent operands type")
            }
            return BinaryExpression(leftOperand, operator, rightOperand)
        }
    }
}

data class ConstExpression(val value: Long) : Expression() {
    override val type = Type.NUMERIC

    override fun toString() = value.toString()

    override fun substitute(expression: Expression) = ConstExpression(value)

    companion object {
        fun parse(input: InputBrowser): ConstExpression? {
            val buf = StringBuilder()
            while (!input.end()) {
                val s = input.consume(1) {
                    it[0].isDigit() || (it == "-" && buf.isEmpty())
                }
                if (s != null) {
                    buf.append(s[0])
                } else {
                    break
                }
            }
            if (buf.isEmpty()) return null
            return ConstExpression(buf.toString().toLong())
        }
    }
}

fun parseExpression(input: InputBrowser): Expression {
    if (input.consume(Element.stringValue)) return Element
    BinaryExpression.parse(input)?.let { return it }
    return ConstExpression.parse(input) ?: throw SyntaxError("Couldn't parse expression")
}
