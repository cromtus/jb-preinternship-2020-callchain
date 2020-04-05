package applicator

import structures.*
import java.lang.RuntimeException


fun applyModelTo(model: Model, array: Iterable<Int>): Iterable<Int> {
    var result = array
    for (call in model.root.calls) {
        result = when (call) {
            is FilterCall -> result.filter { applyFilterTo(call, it) }
            is MapCall -> result.map { applyMapTo(call, it) }
        }
    }
    return result
}

private fun applyFilterTo(filterCall: FilterCall, value: Int) =
    applyBooleanExpressionTo(filterCall.expression, value)

private fun applyMapTo(mapCall: MapCall, value: Int) =
    applyNumericExpressionTo(mapCall.expression, value)

private fun applyBooleanExpressionTo(expression: Expression, value: Int): Boolean {
    if (expression.type == Expression.Type.BOOLEAN) {
        val binaryExpression = expression as BinaryExpression
        return when (binaryExpression.operator.operandsType) {
            Expression.Type.BOOLEAN -> applyBooleanOperator(
                applyBooleanExpressionTo(binaryExpression.leftOperand, value),
                binaryExpression.operator,
                applyBooleanExpressionTo(binaryExpression.rightOperand, value)
            )
            Expression.Type.NUMERIC -> applyBooleanOperator(
                applyNumericExpressionTo(binaryExpression.leftOperand, value),
                binaryExpression.operator,
                applyNumericExpressionTo(binaryExpression.rightOperand, value)
            )
        }
    } else {
        throw RuntimeException("Expected boolean expression")
    }
}

private fun applyBooleanOperator(
    leftOperand: Boolean, operator: Operator, rightOperand: Boolean
) = when (operator) {
        Operator.AND -> leftOperand && rightOperand
        Operator.OR -> leftOperand || rightOperand
        else -> throw RuntimeException("Expected & or |")
    }

private fun applyBooleanOperator(
    leftOperand: Int, operator: Operator, rightOperand: Int
) = when (operator) {
        Operator.GREATER -> leftOperand > rightOperand
        Operator.EQUALS -> leftOperand == rightOperand
        Operator.LESS -> leftOperand < rightOperand
        else -> throw RuntimeException("Expected >, = or <")
    }

private fun applyNumericExpressionTo(expression: Expression, value: Int): Int {
    if (expression.type == Expression.Type.NUMERIC) {
        return when (expression) {
            is Element -> value
            is ConstExpression -> expression.value
            is BinaryExpression -> applyNumericOperator(
                applyNumericExpressionTo(expression.leftOperand, value),
                expression.operator,
                applyNumericExpressionTo(expression.rightOperand, value)
            )
        }
    } else {
        throw RuntimeException("Expected numeric expression")
    }
}

private fun applyNumericOperator(
    leftOperand: Int, operator: Operator, rightOperand: Int
) = when (operator) {
        Operator.PLUS -> leftOperand + rightOperand
        Operator.MINUS -> leftOperand - rightOperand
        Operator.MULT -> leftOperand * rightOperand
        else -> throw RuntimeException("Expected +, - or *")
    }