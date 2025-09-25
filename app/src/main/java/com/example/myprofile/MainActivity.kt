package com.example.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myprofile.ui.theme.MyProfileTheme
import ProfileCard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MyProfileTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        // 🔘 Переключатель темы
                        Button(
                            onClick = { isDarkTheme = !isDarkTheme },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(if (isDarkTheme) "Switch to Light" else "Switch to Dark")
                        }

                        // Профиль тортеу тугел
                        ProfileCard()
                    }
                }
            }
        }
    }
}
