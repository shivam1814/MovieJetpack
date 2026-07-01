package com.shivam.moviejetpack.screens

import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.shivam.moviejetpack.R

val TheaterScreenShape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height
    
    // Create a curved trapezoid shape as seen in the image
    moveTo(0f, height * 0.15f)
    quadraticTo(width / 2f, 0f, width, height * 0.15f) // Top convex curve
    lineTo(width * 0.92f, height) // Right slanted edge
    quadraticTo(width / 2f, height * 0.85f, width * 0.08f, height) // Bottom convex curve
    close()
}

@OptIn(UnstableApi::class)
@Composable
fun SeatSelectScreen(
    ticketCount: Int,
    onBack: () -> Unit = {},
    onContinue: () -> Unit = {}
) {
    val context = LocalContext.current
    val videoUrl = "https://res.cloudinary.com/daw9ly1fj/video/upload/v1776793845/the_bad_guys_trailor_fkm4iv.mp4"
    
    // Initialize main ExoPlayer
    val mainPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    // Initialize reflection ExoPlayer (Muted)
    val reflectionPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 0f // Mute reflection audio
            prepare()
            playWhenReady = true
        }
    }

    // Lifecycle management for ExoPlayers
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    mainPlayer.pause()
                    reflectionPlayer.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    mainPlayer.play()
                    reflectionPlayer.play()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    mainPlayer.release()
                    reflectionPlayer.release()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mainPlayer.release()
            reflectionPlayer.release()
        }
    }

    // Basic sync logic: if they drift apart by more than 100ms, sync reflection to main
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            if (Math.abs(mainPlayer.currentPosition - reflectionPlayer.currentPosition) > 100) {
                reflectionPlayer.seekTo(mainPlayer.currentPosition)
            }
        }
    }

    var seats by remember {
        mutableStateOf(
            List(10) { row ->
                List(8) { col ->
                    if ((row + col) % 5 == 0 || (row == 2 && col < 3)) SeatStatus.BOOKED else SeatStatus.EMPTY
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
        bottomBar = {
            Button(
                onClick = onContinue,
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
            // Header
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Where to sit?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Select seats",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Theater Screen Section with Video and Reflection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Main Screen with Video
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(110.dp)
                            .clip(TheaterScreenShape)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                val view = LayoutInflater.from(ctx).inflate(R.layout.player_view, null, false) as PlayerView
                                view.apply {
                                    player = mainPlayer
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Reflection Effect (Using a flipped copy of the video area)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(70.dp)
                            .graphicsLayer {
                                scaleY = -1f // Flip vertically
                                alpha = 0.25f
                            }
                            .clip(TheaterScreenShape)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                val view = LayoutInflater.from(ctx).inflate(R.layout.player_view, null, false) as PlayerView
                                view.apply {
                                    player = reflectionPlayer
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        // Fading Gradient for reflection
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Black, Color.Transparent),
                                        startY = 0f,
                                        endY = 250f
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                seats.forEachIndexed { rowIndex, row ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEachIndexed { colIndex, status ->
                            SeatIcon(
                                status = status,
                                onClick = {
                                    if (status != SeatStatus.BOOKED) {
                                        val newSeats = seats.map { it.toMutableList() }.toMutableList()
                                        
                                        for (r in newSeats.indices) {
                                            for (c in newSeats[r].indices) {
                                                if (newSeats[r][c] == SeatStatus.SELECTED) {
                                                    newSeats[r][c] = SeatStatus.EMPTY
                                                }
                                            }
                                        }

                                        var selectedInRow = 0
                                        var canFitRight = true
                                        for (i in 0 until ticketCount) {
                                            if (colIndex + i >= 8 || newSeats[rowIndex][colIndex + i] == SeatStatus.BOOKED) {
                                                canFitRight = false
                                                break
                                            }
                                        }

                                        if (canFitRight) {
                                            for (i in 0 until ticketCount) {
                                                newSeats[rowIndex][colIndex + i] = SeatStatus.SELECTED
                                            }
                                            selectedInRow = ticketCount
                                        } else {
                                            var canFitLeft = true
                                            for (i in 0 until ticketCount) {
                                                if (colIndex - i < 0 || newSeats[rowIndex][colIndex - i] == SeatStatus.BOOKED) {
                                                    canFitLeft = false
                                                    break
                                                }
                                            }
                                            if (canFitLeft) {
                                                for (i in 0 until ticketCount) {
                                                    newSeats[rowIndex][colIndex - i] = SeatStatus.SELECTED
                                                }
                                                selectedInRow = ticketCount
                                            } else {
                                                newSeats[rowIndex][colIndex] = SeatStatus.SELECTED
                                                selectedInRow = 1
                                                for (i in 1 until ticketCount) {
                                                    if (colIndex + i < 8 && newSeats[rowIndex][colIndex + i] == SeatStatus.EMPTY) {
                                                        newSeats[rowIndex][colIndex + i] = SeatStatus.SELECTED
                                                        selectedInRow++
                                                    } else break
                                                }
                                                if (selectedInRow < ticketCount) {
                                                    for (i in 1 until (ticketCount - selectedInRow + 1)) {
                                                        if (colIndex - i >= 0 && newSeats[rowIndex][colIndex - i] == SeatStatus.EMPTY) {
                                                            newSeats[rowIndex][colIndex - i] = SeatStatus.SELECTED
                                                            selectedInRow++
                                                        } else break
                                                    }
                                                }
                                            }
                                        }

                                        seats = newSeats.map { it.toList() }
                                        
                                        if (selectedInRow < ticketCount) {
                                            Toast.makeText(context, "${ticketCount - selectedInRow} seat remain to select", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "$ticketCount Seats Together",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Showing where you can sit",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SeatIcon(status: SeatStatus, onClick: () -> Unit) {
    val color = when (status) {
        SeatStatus.EMPTY -> Color.LightGray.copy(alpha = 0.3f)
        SeatStatus.BOOKED -> Color.DarkGray.copy(alpha = 0.5f)
        SeatStatus.SELECTED -> Color(0xFFFFC107)
    }

    Image(
        painter = painterResource(id = R.drawable.img_seat),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp, 28.dp)
            .clickable { onClick() },
        colorFilter = ColorFilter.tint(color)
    )
}

enum class SeatStatus { EMPTY, BOOKED, SELECTED }
