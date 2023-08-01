package com.example.simonsays

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.putString
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.example.simonsays.ui.theme.SimonSaysTheme


class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SimonSaysTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    settings()
                }

            }

        }
    }
}
@Preview
@Composable
fun settings() {
    val options = listOf("Easy", "Normal", "Hard")
    val checkedState = remember { mutableStateListOf(false, true, false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val selectedDifficulty = sharedPreferences.getString("selected_difficulty", "Normal")
        val selectedIndex = options.indexOf(selectedDifficulty)
        if (selectedIndex != -1) {
            checkedState.fill(false)
            checkedState[selectedIndex] = true
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Difficulty",
            fontSize = 30.sp
        )
        Row(

            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            options.forEachIndexed { index, option ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(text = option, style = TextStyle(fontSize = 16.sp))
                        Checkbox(checked = checkedState[index], onCheckedChange = { isChecked ->
                            checkedState[index] = isChecked
                            for (i in 0 until checkedState.size) {
                                if (i != index) {
                                    checkedState[i] = false
                                }
                            }
                            val selectedDifficulty = options[checkedState.indexOf(true)]
                            val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                            sharedPreferences.edit {
                                putString("selected_difficulty", selectedDifficulty)
                            }
                        })


                    }
                }
            }
        }
    }
}