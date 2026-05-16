package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voicemind.R
import com.example.voicemind.ui.theme.VocabMindTheme

data class VocabularyState(
    val learnedWords: Int,
    val totalWords: Int,
    val hasWordsToReview: Boolean,
    val wordsToReviewCount: Int = 0,
    val reviewAfterDays: Int = 0
)
data class VocabItem(
    val word: String,
    val count: Int,
    val level: String? = null, // Ví dụ: C2, B2... (Có thể null)
    val partOfSpeech: String? = null, // Ví dụ: idiom, noun... (Có thể null)
    val ukPhonetic: String? = null,
    val usPhonetic: String? = null,
    val meaning: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDetailScreen(
    onBackClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    var hasWordsToReview by remember { mutableStateOf(false) }

    val state = if (hasWordsToReview) {
        VocabularyState(16, 21, true, 14, 0)
    } else {
        VocabularyState(2, 10, false, 2, 1)
    }
    val vocabList = listOf(
        VocabItem(
            word = "be in the mood",
            count = 6,
            level = "C2",
            partOfSpeech = "idiom",
            ukPhonetic = "/bi ɪn ðə muːd/",
            usPhonetic = "/bi ɪn ðə muːd/",
            meaning = "To feel like doing or having something"
        ),
        VocabItem(
            word = "couldn't help but be aware of",
            count = 2,
            ukPhonetic = "", // Để trống hoặc null tuỳ logic của bạn
            usPhonetic = "",
            meaning = "không thể không nhận thức được"
        ),
        VocabItem(
            word = "vibe",
            count = 6,
            partOfSpeech = "noun",
            ukPhonetic = "/vaɪbz/",
            usPhonetic = "/vaɪbz/",
            meaning = "(động từ số ít hoặc số nhiều)(khẩu ngữ) như vibraphone"
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.advanced_psychology),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF4B5563)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add_ellipse),
                                contentDescription = null,
                                tint = Color(0xFF673AB7),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ProgressCard(state.learnedWords, state.totalWords)
                    Spacer(modifier = Modifier.height(24.dp))
                    ReviewActionCard(state)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (state.hasWordsToReview) {
                    item {
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                            border = null,
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text("Learn new words", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Nút Flashcards
                item {
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        border = null,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cards_solid),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF673AB7)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Flashcards", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Thanh Search
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search by word or definition", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )
                }
                items(vocabList) { item ->
                    VocabCard(item = item)
                }
            }
        }
    }
}

@Composable
fun ProgressCard(learned: Int, total: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Learned", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                            ) {
                                append("$learned")
                            }
                            withStyle(style = SpanStyle(fontSize = 20.sp, color = Color.Gray)) {
                                append("/$total")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.VisibilityOff, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Skipped words",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }

                // Vòng tròn Progress
                val progress = learned.toFloat() / total.toFloat()
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color(0xFFE5E7EB),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = if(progress > 0.5f) Color(0xFF673AB7) else Color(0xFF84CC16),
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Memory Levels", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Giả lập Chart
            MockMemoryChart()
        }
    }
}

@Composable
fun ReviewActionCard(state: VocabularyState) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6D9FF)), // Màu xanh nhạt
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state.hasWordsToReview) "It's time to review" else "You need to review",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (state.hasWordsToReview) {
                    // Trạng thái Hình 1
                    Text("${state.wordsToReviewCount} words", color = Color(0xFFEF4444), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.9f).height(48.dp)
                    ) {
                        Text("Review now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Trạng thái Hình 2
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFF673AB7))) { append("${state.wordsToReviewCount} words ") }
                            withStyle(style = SpanStyle(color = Color(0xFF673AB7))) { append("after ${state.reviewAfterDays} days") }
                        },
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Meanwhile you can learn\nnew words", fontSize = 14.sp, fontStyle = FontStyle.Italic)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.9f).height(48.dp)
                    ) {
                        Text("Learn new words", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Cột bên phải chứa hình minh họa (Giả lập bằng các Box/Icon)
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.EditNote, contentDescription = "Illustration", modifier = Modifier.fillMaxSize(), tint = Color(0xFFFCD34D))
            }
        }
    }
}

@Composable
fun MockMemoryChart() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val levels = listOf("1", "2", "3", "4", "5", "Mastered")
        levels.forEachIndexed { index, label ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                val height = if (label == "Mastered") 80.dp else if (label == "2") 10.dp else 2.dp
                val color = if (label == "Mastered") Color(0xFF673AB7) else if (label == "2") Color(0xFFFBBF24) else Color(0xFFE5E7EB)

                Text("0,0", fontSize = 10.sp, color = Color.Gray) // Dummy text
                Box(modifier = Modifier.width(24.dp).height(height).background(color))
                Spacer(modifier = Modifier.height(8.dp))
                Text(label, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun VocabCard(item: VocabItem) {
    val primaryBlue = Color(0xFF2B70FF) // Màu xanh dương chủ đạo

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Word, Count, Star, More options
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(8f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = item.word,
                            color = primaryBlue,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(2f, fill = false)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Number Badge
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(2.dp, primaryBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.count.toString(),
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Outlined.StarOutline,
                    contentDescription = "Favorite",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Level & Part of Speech Row
            if (item.level != null || item.partOfSpeech != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    if (item.level != null) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF333333), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.level,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (item.partOfSpeech != null) {
                        Text(
                            text = item.partOfSpeech,
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Pronunciation
            if (item.ukPhonetic != null) {
                PronunciationRow(region = "UK", phonetic = item.ukPhonetic)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (item.usPhonetic != null) {
                PronunciationRow(region = "US", phonetic = item.usPhonetic)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Meaning
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "🇺🇸", fontSize = 16.sp) // Dùng emoji làm cờ Mỹ cho đơn giản
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.meaning,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "more",
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun PronunciationRow(region: String, phonetic: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = region,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
            contentDescription = "Listen $region",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = phonetic,
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SetDetailScreenPreview() {
    VocabMindTheme {
        SetDetailScreen()
    }
}