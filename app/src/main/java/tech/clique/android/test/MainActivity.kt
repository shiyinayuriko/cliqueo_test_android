package tech.clique.android.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import tech.clique.android.test.ui.screen.AppNavigation
import tech.clique.android.test.ui.theme.CliqueoTestAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CliqueoTestAndroidTheme {
                AppNavigation()
            }
        }
    }
}