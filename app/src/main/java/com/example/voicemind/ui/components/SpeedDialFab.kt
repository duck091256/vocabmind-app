package com.example.voicemind.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.voicemind.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SpeedDialFab(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onCreateNew: () -> Unit,
    onDownload: () -> Unit,
    onAddFromFriends: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(300),
        label = "fab_rotation"
    )

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(200)) + slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(200)),
            exit = fadeOut(tween(200)) + slideOutVertically(targetOffsetY = { 50 }, animationSpec = tween(200))
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                FabOptionItem("Add sets from friends", painterResource(R.drawable.ic_friend), onAddFromFriends)
                FabOptionItem("Download sets", painterResource(R.drawable.ic_collection), onDownload)
                FabOptionItem("Create new sets", painterResource(R.drawable.ic_add_folder), onCreateNew)
            }
        }

        FloatingActionButton(
            onClick = onToggle,
            containerColor = Color(0xFF6200EE),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotation)
            )
        }
    }
}

@Composable
fun FabOptionItem(title: String, icon: Painter, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
//        Text(
//            text = title,
//            color = Color.White,
//            modifier = Modifier
//                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
//                .padding(horizontal = 12.dp, vertical = 6.dp)
//        )
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.White,
            contentColor = Color(0xFF6200EE),
            shape = CircleShape
        ) {
            Icon(painter = icon, contentDescription = title, modifier = Modifier.size(24.dp))
        }
    }
}