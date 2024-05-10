package com.heixss.findmypet.presenter.common

sealed class NavigationEvent {
    data object BottomNav : NavigationEvent()
    data object LogIn: NavigationEvent()
}