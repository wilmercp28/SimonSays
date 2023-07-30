package com.example.simonsays

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModel : ViewModel(){
    var isPlayerTurn by mutableStateOf(true)
    var level by mutableStateOf(1)
    var timer by mutableStateOf(10)


    fun onBoxClick(boxIndex: Int, boxColors: MutableMap<Int, Color>) {
      BoxChangeColor(boxIndex,boxColors)
    }

    fun BoxChangeColor(boxIndex: Int, boxColors: MutableMap<Int, Color>){
       viewModelScope.launch {
           boxColors[boxIndex] = Color.Blue
           delay(200)
           boxColors[boxIndex] = Color.Green
       }
    }
}