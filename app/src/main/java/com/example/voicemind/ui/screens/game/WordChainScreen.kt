package com.example.voicemind.ui.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordChainScreen(
    onBack: () -> Unit = {},
    viewModel: WordChainViewModel = hiltViewModel()
) {
    val currentWord by viewModel.currentWord.collectAsState()
    val score by viewModel.score.collectAsState()
    val chain by viewModel.chain.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var userInput by remember { mutableStateOf("") }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score", style = MaterialTheme.typography.headlineSmall)
            if (isGameOver) {
                Button(onClick = { viewModel.startNewGame() }) {
                    Text("New Game")
                }
            } else {
                OutlinedButton(onClick = onBack) {
                    Text("Exit")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Current word display
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = currentWord.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (!isGameOver) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Word starting with '${currentWord.last().uppercase()}'") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (viewModel.submitWord(userInput)) {
                            userInput = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }
                OutlinedButton(
                    onClick = { viewModel.endGame() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Give up")
                }
            }
        }

        // Message feedback
        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (it.startsWith("✓"))
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(12.dp),
                    color = if (it.startsWith("✓"))
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // History chain
        if (chain.isNotEmpty()) {
            Text("Word Chain History", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(chain.reversed()) { word ->
                    Text(
                        text = if (word == chain.last()) "→ $word" else "  $word",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}