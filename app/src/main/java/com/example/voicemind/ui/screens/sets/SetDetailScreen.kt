package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.voicemind.R
import com.example.voicemind.domain.model.ForgettingStats
import com.example.voicemind.domain.model.Resource
import com.example.voicemind.ui.screens.spaced_repetition.LevelProgressIcon
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

@Composable
fun SetDetailScreen(
    setId: String,
    onBackClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onAddWordsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SetDetailViewModel = hiltViewModel()
) {
    val wordsResource by viewModel.wordsResource.collectAsStateWithLifecycle()
    val setResource by viewModel.setResource.collectAsStateWithLifecycle()
    val forgettingStats by viewModel.forgettingStats.collectAsStateWithLifecycle()
    val wordProgressMap by viewModel.wordProgressMap.collectAsStateWithLifecycle()

    val setTitle = if (setResource is Resource.Success) {
        (setResource as Resource.Success).data.title
    } else {
        "Loading..."
    }

    LaunchedEffect(wordsResource) {
        if (wordsResource is Resource.Success) {
            // Ép kiểu data theo model thực tế của bạn
            viewModel.loadStatsForSet((wordsResource as Resource.Success).data)
        }
    }

    val vocabList = remember(wordsResource, wordProgressMap) {
        if (wordsResource is Resource.Success) {
            (wordsResource as Resource.Success).data.map { word ->
                val progress = wordProgressMap[word.word.lowercase()]
                val level = progress?.level ?: 0
                val levelStr = if (level == 6) "Mastered" else if (level > 0) "Level $level" else "New"
                VocabItem(
                    word = word.word,
                    count = level,
                    level = levelStr,
                    partOfSpeech = null,
                    ukPhonetic = null,
                    usPhonetic = null,
                    meaning = word.meaning
                )
            }
        } else {
            emptyList()
        }
    }

    val learnedWordsCount = wordProgressMap.values.count { it.level > 0 }
    val totalWordsCount = if (wordsResource is Resource.Success) (wordsResource as Resource.Success).data.size else 0

    val nowSeconds = System.currentTimeMillis() / 1000
    val wordsToReviewCount = wordProgressMap.values.count { it.nextReviewDate.seconds <= nowSeconds }
    val hasWordsToReview = wordsToReviewCount > 0
    val reviewAfterDays = if (!hasWordsToReview && learnedWordsCount > 0) {
        val minNext = wordProgressMap.values.minOfOrNull { it.nextReviewDate.seconds } ?: nowSeconds
        val diffDays = (minNext - nowSeconds) / (60 * 60 * 24)
        diffDays.toInt().coerceAtLeast(1)
    } else 0

    val state = VocabularyState(
        learnedWords = learnedWordsCount,
        totalWords = totalWordsCount,
        hasWordsToReview = hasWordsToReview,
        wordsToReviewCount = wordsToReviewCount,
        reviewAfterDays = reviewAfterDays
    )

    // Truyền dữ liệu xuống hàm UI
    SetDetailContent(
        setTitle = setTitle,
        vocabList = vocabList,
        state = state,
        forgettingStats = forgettingStats,
        onNavigateBack = onNavigateBack,
        onAddWordsClick = onAddWordsClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDetailContent(
    setTitle: String,
    vocabList: List<VocabItem>,
    state: VocabularyState,
    forgettingStats: ForgettingStats?,
    onNavigateBack: () -> Unit,
    onAddWordsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = setTitle,
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
                    IconButton(onClick = onAddWordsClick) {
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
                    ProgressCard(state.learnedWords, state.totalWords, forgettingStats)
                    if(state.totalWords > 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                        ReviewActionCard(state)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
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
                            painter = painterResource(id = R.drawable.ic_cards_solid),
                            contentDescription = null,
                            tint = Color(0xFF673AB7),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Flashcards", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

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
fun ProgressCard(learned: Int, total: Int, stats: ForgettingStats?) {
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
                val progress = if (total > 0) learned.toFloat() / total.toFloat() else 0f
                val progressColor = when {
                    progress < 0.16f -> Color(0xFFE67E22)
                    progress < 0.32f -> Color(0xFFF1C40F)
                    progress < 0.5f  -> Color(0xFFA9DFBF)
                    progress < 0.66f -> Color(0xFF27AE60)
                    progress < 0.83f -> Color(0xFF3498DB)
                    else             -> Color(0xFF2980B9)
                }
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    LevelProgressIcon(
                        level = "",
                        progress = progress,
                        color = progressColor,
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 12.dp.value,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Memory Levels", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Chart that respects the set stats
            MemoryChart(stats)
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
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))

                    if(state.learnedWords == 0) {
                        Text(
                            text = "Start learning to",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = if (state.hasWordsToReview) "It's time to review" else "You need to review",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                if (state.hasWordsToReview) {
                    // Trạng thái Hình 1
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("${state.wordsToReviewCount} words", color = Color(0xFFEF4444), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(48.dp)
                    ) {
                        Text("Review now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Trạng thái Hình 2
                    if(state.learnedWords == 0) {
                        Text(
                            text = "remember words in your set",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        // Trạng thái Hình 3
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color(0xFF673AB7))) { append("${state.wordsToReviewCount} ") }
                                withStyle(style = SpanStyle(color = Color.Black)) { append("words ") }
                                withStyle(style = SpanStyle(color = Color(0xFF673AB7))) { append("after ${state.reviewAfterDays} days") }
                            },
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Meanwhile you can learn\nnew words", fontSize = 14.sp, fontStyle = FontStyle.Italic)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(48.dp)
                    ) {
                        if(state.learnedWords == 0) {
                            Text("Learn now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Learn new words", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
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
fun MemoryChart(stats: ForgettingStats?) {
    // Dữ liệu từ stats hoặc mô phỏng nếu chưa có
    val s = stats ?: ForgettingStats()
    val data = listOf(
        "1" to s.level1.toFloat(),
        "2" to s.level2.toFloat(),
        "3" to s.level3.toFloat(),
        "4" to s.level4.toFloat(),
        "5" to s.level5.toFloat(),
        "Mastered" to s.mastered.toFloat()
    )
    val maxValue = data.maxOf { it.second }.coerceAtLeast(1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .drawBehind {
                    val lineCount = 5
                    val spaceBetween = size.height / (lineCount - 1)
                    for (i in 0 until lineCount) {
                        val y = i * spaceBetween
                        drawLine(
                            color = Color(0xFFF3F4F6),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEach { (label, value) ->
                    val ratio = value / maxValue
                    val maxBarHeight = 130.dp
                    val barHeight = maxBarHeight * ratio

                    val barColor = when (label) {
                        "1" -> Color(0xFFE67E22)
                        "2" -> Color(0xFFF1C40F)
                        "3"  -> Color(0xFFA9DFBF)
                        "4" -> Color(0xFF27AE60)
                        "5" -> Color(0xFF3498DB)
                        "Mastered" -> Color(0xFF2980B9)
                        else -> Color.Transparent
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier =
                            if (label == "Mastered") {
                                Modifier
                                    .weight(1.2f)
                                    .height(140.dp)
                            } else {
                                Modifier
                                    .weight(1f)
                                    .height(140.dp)
                            }
                    ) {
                        Text(
                            text = "${value.toInt()}",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(barHeight)
                                .background(
                                    color = barColor,
                                    shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { (label, _) ->
                Box(
                    modifier = if (label == "Mastered") {
                        Modifier
                            .weight(1.2f)
                    } else {
                        Modifier
                            .weight(1f)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        modifier = Modifier,
                        color = Color.Black,
                        maxLines = 1
                    )
                }
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
                        val (wordProgress, wordColor) = when (item.count) {
                            1 -> 0.16f to Color(0xFFE67E22)
                            2 -> 0.33f to Color(0xFFF1C40F)
                            3 -> 0.50f to Color(0xFFA9DFBF)
                            4 -> 0.66f to Color(0xFF27AE60)
                            5 -> 0.83f to Color(0xFF3498DB)
                            6 -> 1.00f to Color(0xFF2980B9)
                            else -> 0f to Color.LightGray
                        }
                        LevelProgressIcon(
                            level = if (item.count > 0) item.count.toString() else "0",
                            progress = wordProgress,
                            color = wordColor,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp.value,
                            fontSize = 12.sp
                        )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetDetailScreenPreview() {
    VocabMindTheme {
        val mockVocabList = listOf(
            VocabItem("Accommodate", 2, "C1", "verb", "/əˈkɒm.ə.deɪt/", "/əˈkɑː.mə.deɪt/", "To provide with a place to live or to be stored in"),
            VocabItem("Exaggerate", 5, "B2", "verb", "/ɪɡˈzædʒ.ə.reɪt/", "/ɪɡˈzædʒ.ə.reɪt/", "To make something seem larger, more important, better, or worse than it really is")
        )

        SetDetailContent(
            setTitle = "Unit 1: Accommodation",
            vocabList = mockVocabList,
            forgettingStats = ForgettingStats(4,6,3,10,6,30),
            onNavigateBack = {},
            onAddWordsClick = {},
            state = VocabularyState(56, 100, true),
            modifier = Modifier
        )
    }
}