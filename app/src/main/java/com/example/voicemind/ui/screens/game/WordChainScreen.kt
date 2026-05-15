package com.example.voicemind.ui.screens.game

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.domain.game.GameMode
import com.example.voicemind.domain.game.Difficulty
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun WordChainScreen(
    onBack: () -> Unit,
    viewModel: WordChainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentWord by viewModel.currentWord.collectAsStateWithLifecycle()
    val playerScore by viewModel.playerScore.collectAsStateWithLifecycle()
    val aiScore by viewModel.aiScore.collectAsStateWithLifecycle()
    val chain by viewModel.chain.collectAsStateWithLifecycle()
    val gameMode by viewModel.gameMode.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val comboMultiplier by viewModel.comboMultiplier.collectAsStateWithLifecycle()
    val currentDifficulty by viewModel.currentDifficulty.collectAsStateWithLifecycle()

    var userInput by remember { mutableStateOf("") }
    var showDifficultyDialog by remember { mutableStateOf(false) }
    val aiLastWord by viewModel.aiLastWord.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word Chain - Đấu với AI", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                actions = {
                    IconButton(onClick = { showDifficultyDialog = true }) {
                        Badge(
                            containerColor = Color(0xFF6200EE),
                            contentColor = Color.White
                        ) { Text("${currentDifficulty.name[0]}") }
                        Icon(Icons.Default.Settings, contentDescription = "Difficulty")
                    }
                    IconButton(onClick = { viewModel.startNewGame() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Game")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score and Status
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6200EE))
                        Text("Bạn", fontWeight = FontWeight.Bold)
                        Text("$playerScore", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
                    }
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFE8E8E8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("VS", fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Android, contentDescription = null, tint = Color(0xFF03DAC5))
                        Text("AI", fontWeight = FontWeight.Bold)
                        Text("$aiScore", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF03DAC5))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Current Word Display with Combo
            AnimatedContent(targetState = currentWord) { word ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Từ hiện tại", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = word.uppercase(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        modifier = Modifier.padding(8.dp)
                    )
                    if (comboMultiplier > 1) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFF9800),
                            modifier = Modifier.scale(1f).animateContentSize()
                        ) {
                            Text(" Combo x$comboMultiplier! ", fontSize = 12.sp, color = Color.White, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Turn Indicator
            val turnText = when (gameMode) {
                GameMode.PLAYER_TURN -> "Lượt của bạn"
                GameMode.AI_TURN -> "AI đang suy nghĩ..."
                GameMode.GAME_OVER -> "Trận đấu kết thúc"
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(20.dp))
                    .background(
                        if (gameMode == GameMode.PLAYER_TURN) Color(0xFF6200EE).copy(alpha = 0.1f) else Color(0xFFE0E0E0)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(turnText, fontWeight = FontWeight.Bold, color = if (gameMode == GameMode.PLAYER_TURN) Color(0xFF6200EE) else Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Nhập từ bắt đầu bằng '${currentWord.last().uppercase()}'") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = gameMode == GameMode.PLAYER_TURN,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF6200EE))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (userInput.isNotBlank()) {
                                viewModel.submitWord(userInput)
                                userInput = ""
                                keyboardController?.hide()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        enabled = gameMode == GameMode.PLAYER_TURN
                    ) {
                        Text("Kiểm tra", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    if (gameMode == GameMode.GAME_OVER) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.startNewGame() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
                        ) {
                            Text("Chơi lại", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Message Feedback
            AnimatedVisibility(visible = message != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            message?.startsWith("✅") == true -> Color(0xFFE8F5E9)
                            message?.startsWith("❌") == true -> Color(0xFFFFEBEE)
                            else -> Color(0xFFE3F2FD)
                        }
                    )
                ) {
                    Text(message ?: "", modifier = Modifier.padding(12.dp), color = Color(0xFF1A1C1E))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Word Chain History
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Lịch sử chuỗi từ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    LazyColumn {
                        items(chain.reversed()) { word ->
                            val isAIMove = word == viewModel.aiLastWord.collectAsState().value
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                if (isAIMove) {
                                    Icon(Icons.Default.Android, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF03DAC5))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(text = if (word == chain.last()) "→ $word" else "  $word")
                            }
                        }
                    }
                }
            }
        }
    }

    // Difficulty Dialog
    if (showDifficultyDialog) {
        AlertDialog(
            onDismissRequest = { showDifficultyDialog = false },
            title = { Text("Chọn độ khó AI") },
            text = {
                Column {
                    Difficulty.values().forEach { diff ->
                        TextButton(onClick = {
                            viewModel.setDifficulty(diff)
                            showDifficultyDialog = false
                        }) {
                            Text(diff.name, modifier = Modifier.fillMaxWidth())
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDifficultyDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}