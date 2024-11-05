package com.example.grihagully

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.grihagully.Admin.AdminDashboard
import com.example.grihagully.Realestate.RealestateDashboard
import com.example.grihagully.User.UserDashboard
import com.example.grihagully.ui.theme.GRIHAGULLYTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GRIHAGULLYTheme {
               mainScreen()
            }
        }


        val type=getSharedPreferences("user", MODE_PRIVATE).getString("type", "")!!

        when(type){
            "admin"-> {
                startActivity(Intent(this, AdminDashboard::class.java))
                finish()
            }
            "User"->{
                startActivity(Intent(this, UserDashboard::class.java))
                finish()
            }
            "Real estate"->{
                startActivity(Intent(this, RealestateDashboard::class.java))
                finish()
            }
        }
    }

    @Composable
    fun mainScreen(modifier: Modifier = Modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        Color.White
                    }
                ),
            contentAlignment = Alignment.Center
        ){


            var condition by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                delay(1000)
                condition = true
            }
            if (condition) {
                startActivity(Intent(applicationContext, Login::class.java))
                finish()
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
        }





    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        GRIHAGULLYTheme {
            mainScreen()
        }
    }
}
