package com.example.tugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

private val buttonLayout = listOf(
    listOf(ButtonData("AC", 2), ButtonData("C", 2)),
    listOf(ButtonData("√x"), ButtonData("x²"), ButtonData("1/x"), ButtonData("x!")),
    listOf(ButtonData("sin"), ButtonData("cos"), ButtonData("tan"), ButtonData("π")),
    listOf(ButtonData("sin⁻¹"), ButtonData("cos⁻¹"), ButtonData("tan⁻¹"), ButtonData("ln")),
    listOf(ButtonData("log"), ButtonData("xʸ"), ButtonData("("), ButtonData(")")),
    listOf(ButtonData("7"), ButtonData("8"), ButtonData("9"), ButtonData("/")),
    listOf(ButtonData("4"), ButtonData("5"), ButtonData("6"), ButtonData("*")),
    listOf(ButtonData("1"), ButtonData("2"), ButtonData("3"), ButtonData("-")),
    listOf(ButtonData("0"), ButtonData("."), ButtonData("="), ButtonData("+"))
)

data class ButtonData(val text: String, val weight: Int = 1)

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 8.dp, vertical = 32.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = input,
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buttonLayout.forEach { rowData ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowData.forEach { button ->
                        CalcButton(
                            text = button.text,
                            modifier = Modifier.weight(button.weight.toFloat())
                        ) {
                            handleButtonClick(input, button.text) { newInput ->
                                input = newInput
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun handleButtonClick(currentInput: String, buttonText: String, updateInput: (String) -> Unit) {
    val newInput = when (buttonText) {
        "AC" -> "0"
        "C" -> if (currentInput == "Error" || currentInput.length <= 1) "0" else currentInput.dropLast(1)
        "=" -> CalculatorLogic.calculate(currentInput)
        "π" -> PI.toString()
        "xʸ" -> addInput(currentInput, "^")
        "√x" -> CalculatorLogic.calculate("sqrt$currentInput")
        "x²" -> CalculatorLogic.calculate("$currentInput^2")
        "1/x" -> CalculatorLogic.calculate("1/$currentInput")
        "x!" -> CalculatorLogic.calculate("${currentInput}!")
        "sin" -> CalculatorLogic.calculate("sin$currentInput")
        "cos" -> CalculatorLogic.calculate("cos$currentInput")
        "tan" -> CalculatorLogic.calculate("tan$currentInput")
        "sin⁻¹" -> CalculatorLogic.calculate("sin⁻¹$currentInput")
        "cos⁻¹" -> CalculatorLogic.calculate("cos⁻¹$currentInput")
        "tan⁻¹" -> CalculatorLogic.calculate("tan⁻¹$currentInput")
        "ln" -> CalculatorLogic.calculate("ln$currentInput")
        "log" -> CalculatorLogic.calculate("log$currentInput")
        else -> addInput(currentInput, buttonText)
    }
    updateInput(newInput)
}

@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(68.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun addInput(current: String, value: String): String {
    return if (current == "0" || current == "Error") value else current + value
}