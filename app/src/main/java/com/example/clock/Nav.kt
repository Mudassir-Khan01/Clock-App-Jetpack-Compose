package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class Nav : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController= rememberNavController()
            NavHost(navController = navController, startDestination = "clock1",
                builder = {
                    composable("clock1",){
                        ClockView(navController)
                    }
                    composable("clock2",){
                        clockWithNeon()
                    }
                })
        }
    }
}

