package com.heixss.findmypet.presenter.common

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Search : Screen("search_screen")
    data object Profile : Screen("profile_screen")
    data object Login : Screen("login_screen")
    data object Register : Screen("register_screen")
    data object BottomNav : Screen("bottom_nav_screen")
    data object Start: Screen("start_screen")
    data object AddPet: Screen("add_pet")
}
