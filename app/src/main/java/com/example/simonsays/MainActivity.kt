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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simonsays.ui.theme.SimonSaysTheme

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
fun SimonSaysGame(viewModel: ViewModel = ViewModel()){
    val boxColors = remember {mutableStateMapOf<Int, Color>()}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(3) { rowIndex ->
            Row(modifier = Modifier) {
                repeat(3) { columnIndex ->
                    val boxIndex = rowIndex * 3 + columnIndex
                    val boxColor = boxColors[boxIndex] ?: Color.Green
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(color = boxColor )
                            .clickable {
                                viewModel.onBoxClick(boxIndex,boxColors)
                            }
                    )
                }
            }
        }
    }

}


