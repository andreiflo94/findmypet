package com.heixss.findmypet.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.heixss.findmypet.common.theme.FindmypetTheme
import com.heixss.findmypet.presenter.start.StartScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindmypetTheme {
                StartScreen()
            }
        }
    }
}