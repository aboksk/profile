package com.example.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myprofile.ui.theme.MyProfileTheme
import com.example.myprofile.ui.theme.FollowersScreen
import ProfileCard

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf("profile") } // 👈 управление экраном

            MyProfileTheme(darkTheme = isDarkTheme) {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(if (currentScreen == "profile") "My Profile" else "Followers") },
                            actions = {
                                Button(
                                    onClick = { isDarkTheme = !isDarkTheme },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(if (isDarkTheme) "Light" else "Dark")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar {
                            Button(
                                onClick = { currentScreen = "profile" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Profile")
                            }
                            Button(
                                onClick = { currentScreen = "followers" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Followers")
                            }
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        when (currentScreen) {
                            "profile" -> ProfileCard(snackbarHost = snackbarHostState)
                            "followers" -> FollowersScreen()
                        }
                    }
                }
            }
        }
    }
}
