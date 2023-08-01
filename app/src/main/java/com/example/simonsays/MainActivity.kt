package com.example.simonsays

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
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
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val difficulty = sharedPreferences.getString("selected_difficulty", "Hard")

        // Set the difficulty in the DifficultyManager singleton
        DifficultyManager.setDifficulty(difficulty ?: "Normal")

        setContent {
            SimonSaysTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!gameLose.value) {
                        SimonSaysGame()
                    } else {
                        LosingScreen()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    var gameLose = mutableStateOf(false)
    var level = 1
    var clickCounter = 0
    val isSimonTurn = mutableStateOf(true)
    var timer by mutableStateOf(10)
    val difficulty = DifficultyManager.getDifficulty()
    var simonSpeed: Long = when (difficulty) {
        "Easy" -> 2000
        "Normal" -> 1000
        "Hard" -> 400
        else -> 1000
    }
    var lives = when (difficulty) {
        "Easy" -> 20
        "Normal" -> 10
        "Hard" -> 5
        else -> 10
    }

    object DifficultyManager {
        private var currentDifficulty: String = "Hard" // Default value is "Hard"

        fun setDifficulty(difficulty: String) {
            currentDifficulty = difficulty
        }

        fun getDifficulty(): String {
            return currentDifficulty
        }
    }

    fun vibratePhone(context: Context, milliseconds: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(milliseconds)
        }
    }

    fun PlaySound(context: Context, toneType: Int, duration: Int) {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME)
        toneGenerator.startTone(toneType, duration)
        toneGenerator.release()
    }

    @Preview
    @Composable
    fun SimonSaysGame() {
        val context = LocalContext.current
        val boxColors = remember { mutableStateMapOf<Int, Color>() }
        DisposableEffect(Unit) {
            val timerTask = Timer()
            timerTask.scheduleAtFixedRate(0L, 1000L) {
                if (!isSimonTurn.value && timer > 0) {
                    timer -= 1
                }
                if (timer == 0) {
                    CheckForWinner(boxColors, context)
                }
            }

            onDispose {
                timerTask.cancel()
            }
        }

        LaunchedEffect(isSimonTurn) {
            if (isSimonTurn.value) {
                SimonActions(boxColors, simonSpeed) {
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
            ) {

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
                                            CheckForWinner(boxColors, context)
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
            ) {
                Text(
                    text = "Time Left\n$timer",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp

                )

            }
        }

    }

    var playerList = mutableListOf<Int>()
    var simonList = mutableListOf<Int>()
    fun CheckForWinner(boxColors: MutableMap<Int, Color>, context: Context) {
        if (simonList == playerList) {
            PlaySound(context, ToneGenerator.TONE_CDMA_MED_PBX_L, 200)
            simonList.clear()
            playerList.clear()
            level++
            timer = 10
            SimonActions(boxColors, simonSpeed) {
                clickCounter = 0
                isSimonTurn.value = false
            }
        } else if (simonList !== playerList && lives == 1) {
            gameLose.value = true
            vibratePhone(context, 500)
        } else if (simonList !== playerList && lives > 0) {
            vibratePhone(context, 500)
            lives--
            simonList.clear()
            playerList.clear()
            timer = 10
            SimonActions(boxColors, simonSpeed) {
                clickCounter = 0
                isSimonTurn.value = false
            }
        }

    }


    fun BoxChangeColor(boxIndex: Int, boxColors: MutableMap<Int, Color>, playerClick: Boolean) {

        if (playerClick) {
            playerList.add(boxIndex)
        } else if (!playerClick) {
            simonList.add(boxIndex)
        }
        CoroutineScope(Dispatchers.Main).launch {
            boxColors[boxIndex] = Color.Blue
            delay(200)
            boxColors[boxIndex] = Color.Green
        }
    }


    fun SimonActions(
        boxColors: MutableMap<Int, Color>,
        simonSpeed: Long,
        onSimonTurnComplete: () -> Unit
    ) {

        CoroutineScope(Dispatchers.Main).launch {
            repeat(level) {
                delay(simonSpeed)
                val boxIndex = Random.nextInt(9)
                BoxChangeColor(boxIndex, boxColors, false)
            }
            onSimonTurnComplete()
        }
    }

    @Preview
    @Composable
    fun LosingScreen() {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                {
                    playerList.clear()
                    simonList.clear()
                    clickCounter = 0
                    level = 1
                    lives = 5
                    isSimonTurn.value = true
                    timer = 10
                    gameLose.value = false
                },
                modifier = Modifier
                    .size(height = 100.dp, width = 300.dp)
            ) {
                Text(
                    text = "Restart",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )

            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                {
                    val intent = Intent(context, MainMenu::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .size(height = 100.dp, width = 300.dp)
            ) {
                Text(
                    text = "Main Menu",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )

            }

        }


    }
}





