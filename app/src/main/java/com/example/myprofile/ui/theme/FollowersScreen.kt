package com.example.myprofile.ui.theme

import com.example.myprofile.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ---------------------------
// 🧩 1. Stories list
// ---------------------------
val stories = listOf("Aruzhan", "Dias", "Abok", "Madi", "Sanzhar", "Aliya")

// ---------------------------
// 🧩 2. Data class for followers
// ---------------------------
data class Follower(
    val name: String,
    val isFollowing: Boolean = false
)

val followersList = listOf(
    Follower("Aruzhan"),
    Follower("Dias"),
    Follower("Abok"),
    Follower("Madi"),
    Follower("Sanzhar"),
    Follower("Aliya"),
    Follower("Alisher"),
    Follower("Dana"),
    Follower("Miras"),
    Follower("Nurgul")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text("Stories", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(stories) { name ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_foreground),
                                contentDescription = "Story avatar",
                                modifier = Modifier.size(70.dp)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(name, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text("Followers", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                var followers by remember { mutableStateOf(followersList) }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(followers, key = { it.name }) { follower ->
                        var isFollowing by remember { mutableStateOf(follower.isFollowing) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_foreground),
                                contentDescription = "Avatar",
                                modifier = Modifier.size(50.dp)
                            )

                            Text(
                                text = follower.name,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp)
                            )

                            Button(onClick = { isFollowing = !isFollowing }) {
                                Text(if (isFollowing) "Following" else "Follow")
                            }

                            Spacer(Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    val removedUser = follower
                                    followers = followers - removedUser

                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${removedUser.name} removed",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            followers = followers + removedUser
                                        }
                                    }
                                }
                            ) {
                                Text("❌")
                            }
                        }
                    }
                }
            }
        }
    }
}
