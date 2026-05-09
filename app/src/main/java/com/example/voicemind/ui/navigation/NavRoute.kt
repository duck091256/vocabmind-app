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
    SETTINGS("settings", R.string.settings, R.drawable.setting);

    companion object {
        const val SET_DETAIL = "set_detail"
        const val CREATE_SET = "create_set"
        const val FRIEND_PACK = "friend_pack"
        const val EXPLORE_SETS = "explore_sets"
    }
}