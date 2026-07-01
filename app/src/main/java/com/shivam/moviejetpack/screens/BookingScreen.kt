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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.shivam.moviejetpack.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookingScreen(
    onClose: () -> Unit = {},
    onBookingContinue: (Int) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    var showOverlays by remember { mutableStateOf(false) }
    var currentStep by remember { mutableIntStateOf(0) } // 0: Date/Time, 1: Who's going
    var ticketCount by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        showOverlays = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            Button(
                onClick = {
                    if (currentStep == 0) {
                        currentStep = 1
                    } else if (currentStep == 1) {
                        onBookingContinue(ticketCount)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Continue",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Close Button / Back Button
            IconButton(
                onClick = {
                    if (currentStep == 1) {
                        currentStep = 0
                    } else {
                        onClose()
                    }
                },
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Header Section (Static)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                val posterShape = RoundedCornerShape(24.dp)
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(id = R.drawable.img_movie_poster),
                        contentDescription = null,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "movie_poster"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .size(110.dp)
                            .clip(posterShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    val titleAlpha by animateFloatAsState(
                        targetValue = if (showOverlays) 1f else 0f,
                        animationSpec = tween(600, delayMillis = 300),
                        label = "TitleAlpha"
                    )

                    Image(
                        painter = painterResource(id = R.drawable.bad_guy_txt),
                        contentDescription = null,
                        modifier = Modifier
                            .graphicsLayer { alpha = titleAlpha }
                            .height(35.dp)
                            .fillMaxWidth(0.8f),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.CenterStart
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "2025 · Animation · 96 min",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.imdb_img),
                            contentDescription = null,
                            modifier = Modifier.height(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "7.7",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(32.dp))

            // Dynamic Part
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "StepTransition"
            ) { step ->
                if (step == 0) {
                    DateAndTimeSelection()
                } else {
                    WhosGoingSelection(
                        ticketCount = ticketCount,
                        onCountChange = { ticketCount = it }
                    )
                }
            }
        }
    }
}

@Composable
fun DateAndTimeSelection() {
    Column {
        Text(
            text = "When to Watch?",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Select date and time",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Date Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateItem("11", "T", false)
            DateItem("12", "W", false)
            DateItem("13", "T", true)
            DateItem("14", "F", false)

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Time Slots
        TimeSlotRow(listOf("10:45 AM", ""), -1)
        Spacer(modifier = Modifier.height(12.dp))
        TimeSlotRow(listOf("02:45 PM", ""), -1, hasDot = true)
        Spacer(modifier = Modifier.height(12.dp))
        TimeSlotRow(listOf("08:00 PM", ""), 0)

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun WhosGoingSelection(ticketCount: Int, onCountChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = "Who's going?",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Select tickets amount",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Lottie Animations Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..3) {
                    AnimatedVisibility(
                        visible = i <= ticketCount,
                        enter = scaleIn(animationSpec = tween(300)) + fadeIn(),
                        exit = scaleOut(animationSpec = tween(300)) + fadeOut()
                    ) {
                        LottieSmile(i)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (ticketCount > 1) onCountChange(ticketCount - 1) },
                enabled = ticketCount > 1,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (ticketCount > 1) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f))
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = if (ticketCount > 1) Color.White else Color.Gray
                )
            }

            Text(
                text = ticketCount.toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            IconButton(
                onClick = { if (ticketCount < 3) onCountChange(ticketCount + 1) },
                enabled = ticketCount < 3,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (ticketCount < 3) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = if (ticketCount < 3) Color.White else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun LottieSmile(index: Int) {
    val resId = when (index) {
        1 -> R.raw.smile_1
        2 -> R.raw.smile_2
        3 -> R.raw.smile_3
        else -> R.raw.smile_1
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun DateItem(day: String, weekday: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFFFFC107) else Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                color = if (isSelected) Color.Black else Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = weekday,
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TimeSlotRow(times: List<String>, selectedIndex: Int, hasDot: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        times.forEachIndexed { index, time ->
            if (time.isNotEmpty()) {
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.15f))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = time,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (hasDot) {
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                )
            }
            if (index < times.size - 1) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}
