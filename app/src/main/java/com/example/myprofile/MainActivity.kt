package com.example.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myprofile.ui.theme.MyProfileTheme
import ProfileCard

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MyProfileTheme(darkTheme = isDarkTheme) {
                val snackbarHostState = remember { SnackbarHostState()}
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {Text("My Profile")},
                            actions = {
                                Button(
                                    onClick = { isDarkTheme = !isDarkTheme },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(if (isDarkTheme) "Switch to Light" else "Switch to Dark")
                                }
                            }
                        )


                    },
                    snackbarHost = {SnackbarHost(snackbarHostState)}
                ){innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            // 🔘 Переключатель темы

                            // Профиль тортеу тугел
                            ProfileCard(snackbarHost = snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}
