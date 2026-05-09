package com.example.voicemind.ui.screens.sets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voicemind.R
import com.example.voicemind.ui.navigation.AppBottomNavigationBar
import com.example.voicemind.ui.navigation.NavRoute
import com.example.voicemind.ui.theme.VocabMindTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreSetsScreen(
    onBackClick: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onTopicClick: (String) -> Unit = {},
    onSetClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

            SearchBar(
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(
                title = "Best choice of day",
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bestChoices) { choice ->
                    FeaturedSetCard(
                        title = choice.title,
                        description = choice.description,
                        termsCount = choice.termsCount,
                        downloads = choice.downloads,
                        onClick = { onSetClick(choice.title) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Other Section with see all button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SectionHeader(
                    title = "Specialized Vocabulary",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Text(
                    text = "See all",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6200EE),
                    modifier = Modifier.padding(end = 24.dp).align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bestChoices) { choice ->
                    FeaturedSetCard(
                        title = choice.title,
                        description = choice.description,
                        termsCount = choice.termsCount,
                        downloads = choice.downloads,
                        onClick = { onSetClick(choice.title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(
                title = "Topic lists for you",
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TopicFlowRow(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                topics = topics,
                onTopicClick = onTopicClick
            )
            
            Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SearchBar(
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search vocabulary sets...",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun FeaturedSetCard(
    title: String,
    description: String,
    termsCount: Int,
    downloads: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0E0E0), Color.White)
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = 100.dp, y = (-20).dp)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB2EBF2).copy(alpha = 0.6f))
                )
                
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD1C4E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Ciao", fontSize = 8.sp, color = Color(0xFF6200EE))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "VocabMind",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                    Text(
                        text = "Learn smarter with AI",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_list),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = termsCount.toString(), fontSize = 12.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = downloads, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TopicTag(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = borderStroke(width = 1.dp, color = Color(0xFFEEEEEE))
    ) {
        Text(
            text = "#$label",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicFlowRow(
    topics: List<String>,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        topics.forEach { topic ->
            TopicTag(
                label = topic,
                onClick = { onTopicClick(topic) }
            )
        }
    }
}

data class FeaturedSet(
    val title: String,
    val description: String,
    val termsCount: Int,
    val downloads: String
)

val bestChoices = listOf(
    FeaturedSet("Medical Terms Mastery", "Essential vocabulary for...", 250, "12k"),
    FeaturedSet("Legal Vocabulary", "Law and order...", 180, "8k")
)

val topics = listOf(
    "IELTS", "TOEIC", "TOEFL", "PTE", "Oxford", "High school", "Events", "Collocation", "Idioms", "Phrasal Verbs", "Subjects"
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ExploreSetsScreenPreview() {
    VocabMindTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Explore Sets",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF4B5563)
                            )
                        }
                    },
                    actions = {
                        Image(
                            painter = painterResource(id = R.drawable.man),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD591)),
                            contentScale = ContentScale.Crop
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                AppBottomNavigationBar(
                    currentRoute = NavRoute.SETS,
                    onNavigate = {}
                )
            },
            containerColor = Color(0xFFFBFBFF)
        ) { paddingValues ->
            ExploreSetsScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
