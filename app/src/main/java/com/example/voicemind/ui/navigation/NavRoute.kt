package com.example.voicemind.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.voicemind.R

enum class NavRoute(
    val route: String,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    HOME("home", R.string.home, R.drawable.home),
    SETS("sets", R.string.sets, R.drawable.dictionary),
    PROFILE("profile", R.string.profile, R.drawable.user),
    SETTINGS("settings", R.string.settings, R.drawable.setting)
}