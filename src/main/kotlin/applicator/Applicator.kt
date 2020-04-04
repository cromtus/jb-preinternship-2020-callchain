package applicator

import structures.*
import java.lang.RuntimeException


fun applyModelTo(model: Model, array: Iterable<Long>): Iterable<Long> {
    var result = array
    for (call in model.root.calls) {
        result = when (call) {
            is FilterCall -> result.filter { applyFilterTo(call, it) }
            is MapCall -> result.map { applyMapTo(call, it) }
        }
    }
    return result
}

private fun applyFilterTo(filterCall: FilterCall, value: Long) =
    applyBooleanExpressionTo(filterCall.expression, value)

private fun applyMapTo(mapCall: MapCall, value: Long) =
    applyNumericExpressionTo(mapCall.expression, value)

private fun applyBooleanExpressionTo(expression: Expression, value: Long): Boolean {
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

private fun applyBooleanOperator(leftOperand: Boolean, operator: Operator, rightOperand: Boolean) =
    when (operator) {
        Operator.AND -> leftOperand && rightOperand
        Operator.OR -> leftOperand || rightOperand
        else -> throw RuntimeException("Expected & or |")
    }

private fun applyBooleanOperator(leftOperand: Long, operator: Operator, rightOperand: Long) =
    when (operator) {
        Operator.GREATER -> leftOperand > rightOperand
        Operator.EQUALS -> leftOperand == rightOperand
        Operator.LESS -> leftOperand < rightOperand
        else -> throw RuntimeException("Expected >, = or <")
    }

private fun applyNumericExpressionTo(expression: Expression, value: Long): Long {
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

private fun applyNumericOperator(leftOperand: Long, operator: Operator, rightOperand: Long) =
    when (operator) {
        Operator.PLUS -> leftOperand + rightOperand
        Operator.MINUS -> leftOperand - rightOperand
        Operator.MULT -> leftOperand * rightOperand
        else -> throw RuntimeException("Expected +, - or *")
    }