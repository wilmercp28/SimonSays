package com.example.simonsays

import android.graphics.Paint.Align
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.simonsays.ui.theme.SimonSaysTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            SimonSaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!gameLose.value){
                    SimonSaysGame()
                } else{
                    LosingScreen()
                }
                }
            }
        }
    }
}
var gameLose = mutableStateOf(false)
var level = 1
var clickCounter = 0
val isSimonTurn =  mutableStateOf(true)
var timer by mutableStateOf(10)
var lives = 5

@Preview
@Composable
fun SimonSaysGame() {
    val boxColors = remember { mutableStateMapOf<Int, Color>() }


    DisposableEffect(Unit) {
        val timerTask = Timer()
        timerTask.scheduleAtFixedRate(0L, 1000L) {
            if (!isSimonTurn.value && timer > 0) {
                timer -= 1
            }
            if (timer == 0){
                CheckForWinner(boxColors)
            }
        }

        onDispose {
            timerTask.cancel()
        }
    }

LaunchedEffect(isSimonTurn) {
        if (isSimonTurn.value) {
            SimonActions(boxColors) {
                isSimonTurn.value = false // Simon's turn is complete, allow the player to click
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(

            modifier = Modifier
                .size(200.dp),
            Alignment.Center
        ){

            Text(

                text = "$level\nLevel\nLives Left\n$lives",
                textAlign = TextAlign.Center,
                fontSize = 30.sp

            )

        }
        repeat(3) { rowIndex ->
            Row(
                modifier = Modifier
                    .padding(5.dp)
            ) {
                repeat(3) { columnIndex ->
                    val boxIndex = rowIndex * 3 + columnIndex
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(100.dp)
                            .background(
                                color = boxColors[boxIndex] ?: Color.Green,
                                RoundedCornerShape(10)
                            )
                            .border(5.dp, Color.Black, RoundedCornerShape(10))
                            .clickable {
                                if (!isSimonTurn.value) {
                                    BoxChangeColor(boxIndex, boxColors, true)
                                    clickCounter++
                                    if (level == clickCounter) {
                                        CheckForWinner(boxColors)
                                        isSimonTurn.value = true
                                    }
                                }
                            }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(100.dp),
            Alignment.Center
        ){
            Text(text = "Time Left\n$timer",
                textAlign = TextAlign.Center,
                fontSize = 20.sp

            )

        }
    }

}

var playerList = mutableListOf<Int>()
var simonList = mutableListOf<Int>()
fun CheckForWinner(boxColors: MutableMap<Int, Color>) {
    if(simonList == playerList){
        simonList.clear()
        playerList.clear()
        level++
        timer = 10
        SimonActions(boxColors) {
            clickCounter = 0
            isSimonTurn.value = false
        }
    } else if (simonList !== playerList && lives == 0) {
        gameLose.value = true
    } else if (simonList !== playerList && lives > 0){
        lives--
        simonList.clear()
        playerList.clear()
        timer = 10
        SimonActions(boxColors) {
            clickCounter = 0
            isSimonTurn.value = false
        }
    }

}


fun BoxChangeColor(boxIndex: Int, boxColors: MutableMap<Int, Color>,playerClick: Boolean){

    if(playerClick){
        playerList.add(boxIndex)
    } else if (!playerClick){
        simonList.add(boxIndex)
    }
        CoroutineScope(Dispatchers.Main).launch{
            boxColors[boxIndex] = Color.Blue
            delay(200)
            boxColors[boxIndex] = Color.Green

        }
    Log.d("PlayerList","$playerList")
    Log.d("SimonList","$simonList")
    }


    fun SimonActions(boxColors: MutableMap<Int, Color>, onSimonTurnComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            repeat(level) {
                delay(1000)
                val boxIndex = Random.nextInt(9)
                BoxChangeColor(boxIndex, boxColors,false)
            }
            onSimonTurnComplete()
        }

    }
@Preview
@Composable
fun LosingScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(height = 100.dp, width = 300.dp)
                .background(Color.Green, RoundedCornerShape(20.dp))
                .border(5.dp, Color.Black, RoundedCornerShape(10))
                .clickable {
                    playerList.clear()
                    simonList.clear()
                    clickCounter = 0
                    level = 1
                    lives = 5
                    isSimonTurn.value = true
                    timer = 10
                    gameLose.value = false
                },
            Alignment.Center
        ){
            Text(text = "Restart",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )

        }
        Spacer(modifier = Modifier.size(20.dp))
        Box(
            modifier = Modifier
                .size(height = 100.dp, width = 300.dp)
                .background(Color.Green, RoundedCornerShape(20.dp))
                .border(5.dp, Color.Black, RoundedCornerShape(10)),
            Alignment.Center
        ){
            Text(text = "Main Menu",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )

        }
        
    }


}





