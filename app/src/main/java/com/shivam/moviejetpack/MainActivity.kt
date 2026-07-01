package com.shivam.moviejetpack

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.shivam.moviejetpack.screens.BookingScreen
import com.shivam.moviejetpack.screens.HomeScreen
import com.shivam.moviejetpack.screens.SeatSelectScreen
import com.shivam.moviejetpack.ui.theme.MovieJetpackTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        
        setContent {
            MovieJetpackTheme {
                var currentScreen by remember { mutableStateOf("home") }
                var selectedTicketCount by remember { mutableIntStateOf(1) }

                SharedTransitionLayout {
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                EnterTransition.None togetherWith ExitTransition.None
                            }   ,
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                "home" -> HomeScreen(
                                    onBuyTicketsClick = { currentScreen = "booking" },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                                "booking" -> BookingScreen(
                                    onClose = { currentScreen = "home" },
                                    onBookingContinue = { count ->
                                        selectedTicketCount = count
                                        currentScreen = "seat_select"
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                                "seat_select" -> SeatSelectScreen(
                                    ticketCount = selectedTicketCount,
                                    onBack = { currentScreen = "booking" },
                                    onContinue = { /* Final flow */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
