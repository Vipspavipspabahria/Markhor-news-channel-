package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.MarkhorMainScreen
import com.example.ui.MarkhorNewsViewModel
import com.example.ui.theme.MarkhorNewsTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MarkhorNewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarkhorNewsTheme {
                MarkhorMainScreen(viewModel = viewModel)
            }
        }
    }
}
