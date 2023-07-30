package com.example.simonsays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.simonsays.ui.theme.SimonSaysTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimonSaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimonSaysGame()

                }
            }
        }
    }
}

@Preview
@Composable
fun SimonSaysGame() {
    val isSimonTurn = remember { mutableStateOf(true) }
    val boxColors = remember { mutableStateMapOf<Int, Color>() }
    LaunchedEffect(isSimonTurn.value) {
        if (isSimonTurn.value) {
            SimonActions(boxColors) {
                isSimonTurn.value = false // Simon's turn is complete, allow the player to click
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(3) { rowIndex ->
            Row(modifier = Modifier) {
                repeat(3) { columnIndex ->
                    val boxIndex = rowIndex * 3 + columnIndex
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color = boxColors[boxIndex] ?: Color.Green)
                            .clickable {
                                if (!isSimonTurn.value) {
                                    BoxChangeColor(boxIndex, boxColors)
                                }


                            }
                    )
                }
            }
        }
    }
}
fun BoxChangeColor(boxIndex: Int, boxColors: MutableMap<Int, Color>) {
        CoroutineScope(Dispatchers.Main).launch{
            boxColors[boxIndex] = Color.Blue
            delay(200)
            boxColors[boxIndex] = Color.Green

        }
    }


    fun SimonActions(boxColors: MutableMap<Int, Color>, onSimonTurnComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(5) {
                delay(1000)
                val boxIndex = Random.nextInt(9)
                BoxChangeColor(boxIndex, boxColors)
            }
            onSimonTurnComplete()
        }

    }





