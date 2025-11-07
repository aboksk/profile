package com.example.myprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprofile.ui.theme.MyProfileTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "Abdurrakhman Tolegen",
    val bio: String = "Future Web Developer 💻",
    val followers: Int = 776,
    val isFollowing: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun follow() = _uiState.update {
        it.copy(followers = it.followers + 1, isFollowing = true)
    }

    fun unfollow() = _uiState.update {
        it.copy(followers = it.followers - 1, isFollowing = false)
    }

    fun updateProfile(name: String, bio: String) = _uiState.update {
        it.copy(name = name, bio = bio)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MyProfileTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("My Profile App") },
                            actions = {
                                Button(onClick = { isDarkTheme = !isDarkTheme }) {
                                    Text(if (isDarkTheme) "Light" else "Dark")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "profile",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("profile") {
                            ProfileCard(
                                snackbarHost = remember { SnackbarHostState() },
                                onEditClick = { navController.navigate("edit") },
                                onFollowersClick = { navController.navigate("followers") }
                            )
                        }
                        composable("edit") {
                            EditProfileScreen(onBack = { navController.popBackStack() })
                        }
                        composable("followers") {
                            FollowersScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    snackbarHost: SnackbarHostState,
    onEditClick: () -> Unit,
    onFollowersClick: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val buttonColor by animateColorAsState(
        targetValue = if (state.isFollowing) Color.Green else Color.Blue
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.pic),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
            )

            Spacer(Modifier.height(8.dp))
            Text(state.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(state.bio, fontSize = 16.sp, color = Color.Gray)
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (state.isFollowing) vm.unfollow() else vm.follow()
                    scope.launch {
                        snackbarHost.showSnackbar(
                            if (state.isFollowing) "Unfollowed!" else "Followed!"
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) {
                Text(if (state.isFollowing) "Unfollow" else "Follow")
            }

            Spacer(Modifier.height(8.dp))
            Text("Followers: ${state.followers}")

            Spacer(Modifier.height(20.dp))
            Row {
                Button(onClick = onEditClick) { Text("Edit Profile") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onFollowersClick) { Text("Followers") }
            }
        }
    }
}

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    var name by remember { mutableStateOf(state.name) }
    var bio by remember { mutableStateOf(state.bio) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") })

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                vm.updateProfile(name, bio)
                scope.launch { snackbar.showSnackbar("Profile updated!") }
                onBack()
            }) {
                Text("Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val stories = listOf("Aruzhan", "Dias", "Abok", "Madi", "Sanzhar", "Aliya")
    var followers by remember {
        mutableStateOf(
            stories.map { Follower(it) }
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Followers", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(followers, key = { it.name }) { follower ->
                        var isFollowing by remember { mutableStateOf(follower.isFollowing) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(follower.name, fontSize = 18.sp)

                            Button(onClick = { isFollowing = !isFollowing }) {
                                Text(if (isFollowing) "Following" else "Follow")
                            }

                            Button(
                                onClick = {
                                    val removed = follower
                                    followers = followers - removed
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${removed.name} removed",
                                            actionLabel = "Undo"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            followers = followers + removed
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

data class Follower(val name: String, val isFollowing: Boolean = false)
