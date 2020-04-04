package structures

import parser.BINEXP_BEGIN
import parser.BINEXP_END


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
}

data class BinaryExpression(
    val leftOperand: Expression,
    val operator: Operator,
    val rightOperand: Expression
) : Expression() {
    override val type = operator.resultType

    override fun toString() = "$BINEXP_BEGIN$leftOperand$operator$rightOperand$BINEXP_END"

    override fun substitute(expression: Expression) = BinaryExpression(
        leftOperand.substitute(expression),
        operator,
        rightOperand.substitute(expression)
    )
}

data class ConstExpression(val value: Long) : Expression() {
    override val type = Type.NUMERIC

    override fun toString() = value.toString()

    override fun substitute(expression: Expression) = ConstExpression(value)
}
