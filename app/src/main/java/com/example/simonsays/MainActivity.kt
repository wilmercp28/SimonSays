package com.example.simonsays

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simonsays.ui.theme.SimonSaysTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun SimonSaysGame(){
    var isPlayerTurn = remember {mutableStateOf(true) }
    var level = 1
    var timer = remember {mutableStateOf(10)}
    var selectedBoxIndex by remember { mutableStateOf<Int?>(null) }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        repeat(3){
        Row(modifier = Modifier) {
            repeat(3) {columnIndex ->
                val boxIndex = it * 3 + columnIndex
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(getBoxColor(selectedBoxIndex == boxIndex))
                        .clickable {
                            if (isPlayerTurn.value) {
                                selectedBoxIndex = boxIndex

                            }
                        }
                            )

            }
        }
        }
    }
}
@Composable
fun getBoxColor(isSelected: Boolean): Color {
   var color by remember {
       mutableStateOf(Color.Green)
   }
    LaunchedEffect(isSelected) {
        color = Color.Red
        delay(1000)
        color = Color.Green
    }


    return color
}

