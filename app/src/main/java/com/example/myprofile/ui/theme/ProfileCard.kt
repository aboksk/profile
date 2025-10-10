import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofile.R
import kotlinx.coroutines.launch

@Composable
fun ProfileCard(snackbarHost: SnackbarHostState) {
    var followers by  remember { mutableStateOf(776) }
    var isFollowing by remember { mutableStateOf(false) }
    var showUnfollowDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватар (временно иконка)
            Image(
                painter = painterResource(R.drawable.pic),

                contentDescription = "Avatar",
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(24.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Имя
            Text(
                text = "Abdurrakhman Tolegen",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))


            // Биография
            Text(
                text = "Future Web Developer 💻",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "I’m a person who enjoys coding and constantly learning new things in technology. Outside of that, I love sports and staying active, especially going to the gym. I also enjoy reading books, watching movies, and spending time in the mountains — nature inspires me and helps me recharge.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))


            // Кнопка Follow
            Button(onClick = {
                if (isFollowing) {
                    // 👇 показываем диалог
                    showUnfollowDialog = true
                } else {
                    followers++
                    isFollowing = true
                    scope.launch {
                        snackbarHost.showSnackbar("You followed Abdurrakhman!")
                    }
                }
            }) {
                Text(if (isFollowing) "Unfollow" else "Follow")
            }

            if (showUnfollowDialog) {
                AlertDialog(
                    onDismissRequest = { showUnfollowDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            followers--
                            isFollowing = false
                            showUnfollowDialog = false
                            scope.launch {
                                snackbarHost.showSnackbar("You unfollowed Abdurrakhman.")
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUnfollowDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    text = { Text("Are you sure you want to unfollow?") }
                )
            }



            Text("Followers: $followers")
        }
    }
}
