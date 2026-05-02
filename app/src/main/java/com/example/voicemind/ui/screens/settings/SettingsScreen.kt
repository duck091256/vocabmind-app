package com.example.voicemind.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voicemind.R
import com.example.voicemind.ui.navigation.AppBottomNavigationBar
import com.example.voicemind.ui.navigation.NavRoute
import com.example.voicemind.ui.theme.VocabMindTheme

// TopBar expose ra ngoài để NavGraph gắn vào Scaffold chung
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar() {
    Surface(
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD591)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.man),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            },
            actions = {
                Icon(
                    painter = painterResource(R.drawable.sparkle),
                    contentDescription = null,
                    tint = Color(0xFF6200EE),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }
}

// Content thuần — không Scaffold, không topBar/bottomBar bên trong
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onPasswordClick: () -> Unit = {},
    onDailyGoalClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onHelpCenterClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        Text(
            text = stringResource(R.string.settings_subtitle),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6C727A),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        SettingsSection(title = stringResource(R.string.account)) {
            SettingsItem(
                icon = painterResource(R.drawable.user),
                title = stringResource(R.string.profile_info),
                subtitle = stringResource(R.string.profile_info_desc),
                onClick = onProfileClick,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = Color(0xFFCED4DA)
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F5))
            SettingsItem(
                icon = painterResource(R.drawable.lock_password),
                title = stringResource(R.string.change_password),
                subtitle = stringResource(R.string.change_password_desc),
                onClick = onPasswordClick,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = Color(0xFFCED4DA)
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
        }

        SettingsSection(title = stringResource(R.string.learning_preferences), modifier = Modifier.padding(top = 24.dp)) {
            SettingsItem(
                icon = painterResource(R.drawable.flag),
                title = stringResource(R.string.daily_goal),
                subtitle = stringResource(R.string.daily_goal_desc),
                onClick = onDailyGoalClick,
                trailingIcon = {
                    Text(
                        text = stringResource(R.string.adjust),
                        color = Color(0xFF6200EE),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F5))
            SettingsItem(
                icon = painterResource(R.drawable.language),
                title = stringResource(R.string.native_language),
                subtitle = stringResource(R.string.native_language_desc),
                onClick = onLanguageClick,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = Color(0xFFCED4DA)
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
        }

        SettingsSection(title = stringResource(R.string.notifications), modifier = Modifier.padding(top = 24.dp)) {
            SettingsItem(
                icon = painterResource(R.drawable.notification),
                title = stringResource(R.string.daily_reminder),
                subtitle = stringResource(R.string.daily_reminder_desc),
                trailingIcon = {
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF6200EE),
                            uncheckedBorderColor = Color.Transparent,
                            uncheckedTrackColor = Color(0xFFE9ECEF)
                        )
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F5))
            SettingsItem(
                icon = painterResource(R.drawable.streak_alerts),
                title = stringResource(R.string.streak_alerts),
                subtitle = stringResource(R.string.streak_alerts_desc),
                trailingIcon = {
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF6200EE),
                            uncheckedBorderColor = Color.Transparent,
                            uncheckedTrackColor = Color(0xFFE9ECEF)
                        )
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
        }

        SettingsSection(title = stringResource(R.string.support), modifier = Modifier.padding(top = 24.dp)) {
            SettingsItem(
                icon = painterResource(R.drawable.quiz_icon),
                title = stringResource(R.string.help_center),
                onClick = onHelpCenterClick,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_open_in_new),
                        contentDescription = null,
                        tint = Color(0xFFCED4DA),
                        modifier = Modifier.size(20.dp)
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F3F5))
            SettingsItem(
                icon = painterResource(R.drawable.privacy_policy),
                title = stringResource(R.string.privacy_policy),
                onClick = onPrivacyPolicyClick,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = Color(0xFFCED4DA)
                    )
                },
                iconBackgroundColor = Color(0xFFEDE7F6),
                iconTintColor = Color(0xFF6200EE)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFE03131)
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFFE3E3)))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.log_out),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.log_out),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Text(
            text = stringResource(R.string.version_info),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = Color(0xFF6C727A)
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF6C727A),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    iconBackgroundColor: Color = Color(0xFFF8F9FA),
    iconTintColor: Color = Color(0xFF495057)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconTintColor
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF6C727A)
                )
            }
        }
        trailingIcon()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    VocabMindTheme {
        Scaffold(
            topBar = { SettingsTopBar() },
            bottomBar = {
                AppBottomNavigationBar(
                    currentRoute = NavRoute.SETTINGS,
                    onNavigate = { }
                )
            }
        ) { paddingValues ->
            SettingsScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}