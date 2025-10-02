package com.example.tugas

import kotlin.math.*

object CalculatorLogic {

    private const val MAX_RESULT_LENGTH = 15

    private val scientificFunctions = mapOf(
        "sqrt" to { num: Double -> if (num < 0) Double.NaN else sqrt(num) },
        "sin" to { num: Double -> sin(Math.toRadians(num)) },
        "cos" to { num: Double -> cos(Math.toRadians(num)) },
        "tan" to { num: Double -> tan(Math.toRadians(num)) },
        "ln" to { num: Double -> if (num <= 0.0) Double.NaN else ln(num) },
        "log" to { num: Double -> if (num <= 0.0) Double.NaN else log10(num) },
        "sin⁻¹" to { num: Double -> if (num !in -1.0..1.0) Double.NaN else Math.toDegrees(asin(num)) },
        "cos⁻¹" to { num: Double -> if (num !in -1.0..1.0) Double.NaN else Math.toDegrees(acos(num)) },
        "tan⁻¹" to { num: Double -> Math.toDegrees(atan(num)) }
    )

    fun calculate(expression: String): String {
        return try {
            when {
                expression.startsWith("1/") -> calculateSimpleOperation(expression.removePrefix("1/"), { num -> 1.0 / num }, 0.0)
                expression.endsWith("!") -> {
                    val num = expression.removeSuffix("!").toDoubleOrNull()
                    if (num == null || num < 0 || num % 1 != 0.0) return "Error"
                    factorial(num.toInt()).toString()
                }
                expression.contains("^") -> {
                    val (baseStr, expStr) = expression.split("^", limit = 2)
                    val base = baseStr.toDoubleOrNull() ?: return "Error"
                    val exp = expStr.toDoubleOrNull() ?: return "Error"
                    formatResult(base.pow(exp))
                }
                scientificFunctions.keys.any { expression.startsWith(it) } -> {
                    val functionKey = scientificFunctions.keys.first { expression.startsWith(it) }
                    val num = expression.removePrefix(functionKey).toDoubleOrNull() ?: return "Error"
                    val result = scientificFunctions[functionKey]!!.invoke(num)
                    formatResult(result)
                }
                else -> evaluateExpression(expression)
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun calculateSimpleOperation(input: String, operation: (Double) -> Double, errorCheck: Double? = null): String {
        val num = input.toDoubleOrNull() ?: return "Error"
        if (errorCheck != null && num == errorCheck) return "Error"
        return formatResult(operation(num))
    }

    private fun factorial(n: Int): Long {
        return if (n <= 1) 1L else n * factorial(n - 1)
    }

    private fun formatResult(result: Double): String {
        return if (result.isNaN() || result.isInfinite()) {
            "Error"
        } else {
            val formattedString = if (result % 1.0 == 0.0) {
                result.toLong().toString()
            } else {
                result.toString()
            }

            if (formattedString.length <= MAX_RESULT_LENGTH) {
                formattedString
            } else {
                formattedString.substring(0, MAX_RESULT_LENGTH)
            }
        }
    }

    private fun evaluateExpression(expression: String): String {
        val cleanedExpression = expression.replace(" ", "")
        val operator = cleanedExpression.find { it in listOf('+', '-', '*', '/') }
        if (operator == null) return cleanedExpression

        val parts = cleanedExpression.split(operator, limit = 2)
        if (parts.size < 2) return cleanedExpression

        val num1 = parts[0].toDoubleOrNull() ?: return "Error"
        val num2 = parts[1].toDoubleOrNull() ?: return "Error"

        val result = when (operator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '*' -> num1 * num2
            '/' -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> Double.NaN
        }
        return formatResult(result)
    }
}