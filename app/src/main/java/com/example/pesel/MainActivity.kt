package com.example.pesel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.ui.tooling.preview.Preview
enum class Gender{
    M,
    K,
    Default,
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PreviewGreeting()
        }
    }

    @Composable
    fun TxtField() {
        val isControlSumOk = remember { mutableStateOf<Boolean?>(null) }
        val gender = remember { mutableStateOf<Gender?>(null) }
        val birthDate = remember { mutableStateOf<String?>(null) }
        val isValid = remember { mutableStateOf(false) }
        val inputValue = remember { mutableStateOf(TextFieldValue()) }
        Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TextField(
                    value = inputValue.value,
                    onValueChange = { inputValue.value = it },
                    placeholder = { Text(text = "Enter user name") },
                    modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif),
                    maxLines = 2,
                    activeColor = colorResource(id = R.color.purple_200),
                    singleLine = true
            )
            Button(
                onClick = {
                    val text = inputValue.value.text
                    isValid.value = text.matches(Regex.fromLiteral("""\d{11}"""))

                    gender.value =
                        if (!isValid.value) null else if (text[6] == '1') Gender.M else Gender.K

                    birthDate.value = if (!isValid.value) null else {
                        val month = text.slice(2..3).toInt()
                        val (year, stringMonth) = if (month > 12) Pair("20", (month - 20).toString()) else Pair("19", month.toString())
                        val date = "${year}${text.slice(0..1)}-${stringMonth}-${text.slice(4..5)}"
                        date
                    }

                    isControlSumOk.value =
                        if (!isValid.value) null
                        else{
                            val weights = listOf(1,3,7,9,1,3,7,9,1,3)
                            val sum = weights.mapIndexed { x, i -> x * weights[i]}.sum() % 10
                            (10 - sum) % 10 == text[10].toInt()
                        }
                }
            ){
                androidx.compose.material.Text( text = "Verify")
            }
            RowOf("isValid:", isValid.value)
            RowOf("birt date:", birthDate.value)
            RowOf("gender:", gender.value)
            RowOf("is control sum ok:", isControlSumOk.value)
        }
    }

    @Composable
    fun RowOf(text: String, value: Any?){
        Row{
            BasicText(
                text = text,
                modifier = Modifier.padding(end = 5.dp)
            )
            BasicText("${value ?: ""}")
        }
    }

    @Preview
    @Composable
    fun PreviewGreeting() {
        TxtField()
    }
}
