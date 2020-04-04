package parser

import io.InputBrowser
import structures.*
import java.lang.StringBuilder


fun parseExpression(input: InputBrowser): Expression {
    if (input.consume(Element.stringValue)) return Element
    parseBinaryExpression(input)?.let { return it }
    return parseConstExpression(input) ?: throw SyntaxError("Couldn't parse expression")
}

internal const val BINEXP_BEGIN = "("
internal const val BINEXP_END = ")"

fun parseBinaryExpression(input: InputBrowser): BinaryExpression? {
    if (!input.consume(BINEXP_BEGIN)) return null
    val leftOperand = parseExpression(input)

    val operator = parseOperator(input)

    val rightOperand = parseExpression(input)
    if (!input.consume(BINEXP_END)) throw SyntaxError("Missing $BINEXP_END")

    if (operator.operandsType != leftOperand.type || operator.operandsType != rightOperand.type) {
        throw TypeError("Inconsistent operands types")
    }
    return BinaryExpression(leftOperand, operator, rightOperand)
}

fun parseConstExpression(input: InputBrowser): ConstExpression? {
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

fun parseOperator(input: InputBrowser): Operator {
    for (operator in Operator.values()) {
        if (input.consume(operator.stringValue)) {
            return operator
        }
    }
    throw SyntaxError("Unknown operator")
}