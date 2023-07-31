package com.example.simonsays

import android.app.ListActivity
import android.content.Intent
import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simonsays.ui.theme.SimonSaysTheme

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SimonSaysTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Menu()
                }
                
            }

        }
    }
}
@Preview
@Composable
fun Menu(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainMenu"){
        composable("MainMenu"){ Menu()}
        composable("MainActivity"){MainActivity()}
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    )
    {
        Text(
            text = "Simon Says Game",
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(60.dp))

        Button({
            navController.navigate("MainActivity")
        },
            modifier = Modifier
                .size(height = 100.dp, width = 300.dp)
                .background(Color.Transparent)

        ){
            Text(text = "Start Game",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }
        Button({},
            modifier = Modifier
                .size(height = 100.dp, width = 300.dp)
        ){
            Text(text = "Settings",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }
        Button({},
            modifier = Modifier
                .size(height = 100.dp, width = 300.dp)
        ){
            Text(text = "Quit",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }
        
    }
    
}