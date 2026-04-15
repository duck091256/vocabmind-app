package com.example.voicemind.ui.screens.settings

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
import com.example.voicemind.ui.theme.VocabMindTheme

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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SettingsTopBar()
        },
        bottomBar = {
            SettingsBottomNavigation()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FB))
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
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                            color = Color(0xFF4A378B),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    },
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                                checkedTrackColor = Color(0xFF6C4DDA),
                                uncheckedBorderColor = Color.Transparent,
                                uncheckedTrackColor = Color(0xFFE9ECEF)
                            )
                        )
                    },
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                                checkedTrackColor = Color(0xFF6C4DDA),
                                uncheckedBorderColor = Color.Transparent,
                                uncheckedTrackColor = Color(0xFFE9ECEF)
                            )
                        )
                    },
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
                    iconBackgroundColor = Color(0xFFF1EEFF),
                    iconTintColor = Color(0xFF6C4DDA)
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
}

@Composable
fun SettingsTopBar() {
    Surface(
        color = Color.White,
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE9ECEF))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.man),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "VocabMind",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            }
            Icon(
                painter = painterResource(R.drawable.sparkle),
                contentDescription = null,
                tint = Color(0xFF6C4DDA),
                modifier = Modifier.size(24.dp)
            )
        }
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

@Composable
fun SettingsBottomNavigation() {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(80.dp),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(R.drawable.home), contentDescription = null) },
            label = { Text(stringResource(R.string.home)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFADB5BD),
                unselectedTextColor = Color(0xFFADB5BD)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(R.drawable.book), contentDescription = null) },
            label = { Text(stringResource(R.string.sets)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFADB5BD),
                unselectedTextColor = Color(0xFFADB5BD)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(painter = painterResource(R.drawable.user), contentDescription = null) },
            label = { Text(stringResource(R.string.profile)) },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFADB5BD),
                unselectedTextColor = Color(0xFFADB5BD)
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(painter = painterResource(R.drawable.setting), contentDescription = null) },
            label = { Text(stringResource(R.string.settings)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6C4DDA),
                selectedTextColor = Color(0xFF6C4DDA),
                indicatorColor = Color(0xFFF1EEFF)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    VocabMindTheme {
        SettingsScreen()
    }
}
