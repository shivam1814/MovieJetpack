package com.shivam.moviejetpack.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shivam.moviejetpack.R


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    onBuyTicketsClick: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    // State to trigger internal animations after the screen is composed
    var showOverlays by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showOverlays = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            BottomNavigationBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Now Showing",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.topbar_right),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Movie Poster Card
            val posterShape = RoundedCornerShape(32.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            ) {
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(id = R.drawable.img_movie_poster),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "movie_poster"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .clip(posterShape)
                            .fillMaxSize()
                    )
                }

                // Overlay Gradient with fade animation
                val gradientAlpha by animateFloatAsState(
                    targetValue = if (showOverlays) 1f else 0f,
                    animationSpec = tween(400, delayMillis = 400),
                    label = "GradientAlpha"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = gradientAlpha }
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                                startY = 300f
                            )
                        )
                )

                // Overlay Content on Poster
                val contentAlpha by animateFloatAsState(
                    targetValue = if (showOverlays) 1f else 0f,
                    animationSpec = tween(500, delayMillis = 600),
                    label = "ContentAlpha"
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = contentAlpha }
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*Text(
                        text = "BACK IN BADNESS",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )*/

                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.bad_guy_txt),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(110.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Movie Info
            Text(
                text = "2025 · Animation · 96 min",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.imdb_img),
                    contentDescription = null,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "7.7",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBuyTicketsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC107) // Yellow
                    ),
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Buy Tickets",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White,
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home", modifier = Modifier.size(28.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.Search, contentDescription = "Search", modifier = Modifier.size(28.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Outlined.List, contentDescription = "Library", modifier = Modifier.size(28.dp)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
