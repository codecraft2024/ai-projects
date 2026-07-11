package com.ghosttalk.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ghosttalk.ui.navigation.GhostTalkNavHost
import com.ghosttalk.ui.theme.GhostTalkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GhostTalkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GhostTalkNavHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
