package com.example.voicemind.ui.screens.lessons

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.voicemind.domain.model.Lesson
import com.example.voicemind.domain.model.WordItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class StudyPhase { LEARN, TEST }
enum class TestType { FULL_WRITE, BLANK_FILL }

data class WordProgress(
    val wordItem: WordItem,
    var testStage: TestType = TestType.FULL_WRITE,
    var isCompleted: Boolean = false,
    var blankIndices: List<Int> = emptyList(),   // lưu vị trí các chữ bị ẩn
    var expectedChars: String = ""               // chuỗi các chữ cần nhập (theo thứ tự)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    lesson: Lesson,
    onBack: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val wordProgressList = remember {
        lesson.words.map {
            WordProgress(it)
        }.toMutableStateList()
    }
    var currentIndex by remember { mutableStateOf(0) }
    var phase by remember { mutableStateOf(StudyPhase.LEARN) }
    var userInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showDefinition by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    val currentProgress = wordProgressList.getOrNull(currentIndex)
    val currentWord = currentProgress?.wordItem

    // Hàm tạo vị trí ẩn ngẫu nhiên (2-4 chữ)
    fun generateBlankIndices(word: String): List<Int> {
        val length = word.length
        if (length <= 2) return listOf(0) // nếu từ quá ngắn thì ẩn 1 chữ
        val numBlanks = when {
            length <= 4 -> 2
            length <= 7 -> 3
            else -> 4
        }
        // Chọn các vị trí ngẫu nhiên không trùng nhau
        val indices = mutableListOf<Int>()
        while (indices.size < numBlanks) {
            val rand = Random.nextInt(0, length)
            if (rand !in indices) indices.add(rand)
        }
        return indices.sorted()
    }

    fun getDisplayWord(word: String, blankIndices: List<Int>): String {
        return word.mapIndexed { index, c ->
            if (index in blankIndices) "___" else c.toString()
        }.joinToString(" ")
    }

    fun getExpectedChars(word: String, blankIndices: List<Int>): String {
        return blankIndices.map { word[it].toString() }.joinToString("")
    }

    fun nextWord() {
        if (currentIndex + 1 < wordProgressList.size) {
            currentIndex++
            userInput = ""
            feedbackMessage = null
            showDefinition = false
            phase = StudyPhase.LEARN
        } else {
            showCompletionDialog = true
        }
    }

    fun checkWrite() {
        if (currentWord == null || currentProgress == null) return
        val trimmedInput = userInput.trim().lowercase()
        val correctWord = currentWord.word.lowercase()

        when (currentProgress.testStage) {
            TestType.FULL_WRITE -> {
                if (trimmedInput == correctWord) {
                    // Tạo blank indices và expected chars cho lần kiểm tra thứ hai
                    val blankIndices = generateBlankIndices(correctWord)
                    val expectedChars = getExpectedChars(correctWord, blankIndices)
                    wordProgressList[currentIndex] = currentProgress.copy(
                        testStage = TestType.BLANK_FILL,
                        blankIndices = blankIndices,
                        expectedChars = expectedChars
                    )
                    feedbackMessage = "✅ Tốt! Hãy điền các chữ còn thiếu."
                    userInput = ""
                    scope.launch {
                        delay(1000)
                        feedbackMessage = null
                    }
                } else {
                    feedbackMessage = "❌ Sai. Đáp án: $correctWord. Thử lại!"
                    userInput = ""
                    scope.launch {
                        delay(2000)
                        feedbackMessage = null
                    }
                }
            }
            TestType.BLANK_FILL -> {
                val expected = currentProgress.expectedChars
                if (trimmedInput == expected) {
                    // Hoàn thành từ
                    wordProgressList[currentIndex] = currentProgress.copy(isCompleted = true)
                    viewModel.saveWordProgress(correctWord)
                    feedbackMessage = "✅ Xuất sắc! Hoàn thành từ này."
                    scope.launch {
                        delay(1000)
                        feedbackMessage = null
                        nextWord()
                    }
                } else {
                    feedbackMessage = "❌ Sai. Đáp án đúng: $expected. Thử lại!"
                    userInput = ""
                    scope.launch {
                        delay(2000)
                        feedbackMessage = null
                    }
                }
            }
        }
    }

    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("🎉 Chúc mừng!") },
            text = { Text("Bạn đã hoàn thành bài học \"${lesson.title}\" với ${lesson.words.size} từ.\nHãy tiếp tục học bài mới!") },
            confirmButton = {
                Button(
                    onClick = {
                        val prefs = context.getSharedPreferences("lesson_progress", Context.MODE_PRIVATE)
                        prefs.edit().putBoolean("lesson_${lesson.id}", true).apply()
                        onBack()
                    }
                ) {
                    Text("Về danh sách")
                }
            }
        )
    }

    if (currentWord == null) return

    val total = lesson.words.size
    val completedCount = wordProgressList.count { it.isCompleted }
    val progressFraction = if (total > 0) completedCount.toFloat() / total else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        TopAppBar(
            title = { Text(lesson.title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LinearProgressIndicator(
            progress = progressFraction,
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = Color(0xFF6200EE)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tiến độ: $completedCount / $total từ",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (phase) {
            StudyPhase.LEARN -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(360.dp)
                        .clickable { showDefinition = !showDefinition },
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!showDefinition) {
                            Text(
                                text = currentWord.word.uppercase(),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "👉 Nhấn vào thẻ để xem nghĩa & ví dụ",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        } else {
                            Text(
                                text = currentWord.definition,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "📖 Ví dụ: ${currentWord.example}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { phase = StudyPhase.TEST },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Đã nhớ! Bắt đầu kiểm tra", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            StudyPhase.TEST -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (currentProgress.testStage == TestType.FULL_WRITE) {
                            Text(
                                text = "🔤 Viết lại từ:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1A1C1E)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = currentWord.definition,
                                fontSize = 18.sp,
                                color = Color(0xFF6200EE)
                            )
                        } else {
                            val displayWord = getDisplayWord(currentWord.word, currentProgress.blankIndices)
                            val numBlanks = currentProgress.blankIndices.size
                            Text(
                                text = "✏️ Điền ${numBlanks} chữ còn thiếu:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1A1C1E)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = displayWord,
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF6200EE)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "(Nhập các chữ bị thiếu theo thứ tự, không dấu cách)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    label = {
                        Text(
                            if (currentProgress.testStage == TestType.FULL_WRITE) "Nhập từ tiếng Anh"
                            else "Nhập các chữ còn thiếu"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    singleLine = true,
                    isError = feedbackMessage?.startsWith("❌") == true,
                    supportingText = {
                        if (feedbackMessage != null) {
                            Text(
                                text = feedbackMessage!!,
                                color = if (feedbackMessage!!.startsWith("✅")) Color(0xFF10B981) else Color(0xFFEF4444)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { checkWrite() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Kiểm tra", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}