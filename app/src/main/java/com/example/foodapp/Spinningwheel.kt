package com.example.foodapp

import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinningWheelScreen() {
    val wheelState = remember { Animatable(0f) }
    val rotation by wheelState.asState()
    val coroutineScope = rememberCoroutineScope()
    var selectedAmount by remember { mutableStateOf("") }
    var showCelebration by remember { mutableStateOf(false) }

    var amounts = arrayOf("10", "20", "30", "40", "50")



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spinning Wheel", style = MaterialTheme.typography.headlineSmall) }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.game), // Your background image
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.65f)
                        .align(Alignment.TopCenter), // Align it to the top half of the screen
                    contentScale = ContentScale.Crop // Adjust image scaling
                )}


                Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(contentAlignment = Alignment.Center) {

                    SpinningWheel(rotation = rotation, amounts = amounts)


                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Arrow",
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(100.dp))
                            .rotate(140f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))


                Button(
                    onClick = {

                        showCelebration= false


                        coroutineScope.launch {


                            selectedAmount = spinWheel(wheelState, amounts, targetIndex = 2)
                            showCelebration = true
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Spin")
                }

                if (showCelebration) {
                    CelebrationAnimation(selectedAmount)
                }
            }
        }
    )
}

@Composable
fun SpinningWheel(rotation: Float, amounts: Array<String>) {
    val numberOfSections = amounts.size
    val colors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan
    )
    val sectionAngle = 360f / numberOfSections

    Canvas(modifier = Modifier
        .size(300.dp)
        .clip(CircleShape)) {

        for (i in amounts.indices) {
            rotate(degrees = rotation + i * sectionAngle) {
                drawIntoCanvas {
                    drawRoundRect(
                        color = colors[i % colors.size],
                        topLeft = Offset(size.width / 2, 0f),
                        size = Size(size.width / 2, size.height / 2),
                        cornerRadius = CornerRadius.Zero
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        val text = amounts[i]
                        drawText(
                            text,
                            size.width / 4,
                            size.height / 4,
                            Paint().apply {
                                color = android.graphics.Color.WHITE
                                textSize = 40f
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }

        drawCircle(
            color = Color.White,
            radius = size.minDimension / 8f,
            center = Offset(size.width / 2, size.height / 2)
        )
    }
}

// Coroutine to spin the wheel and land on a specific index
suspend fun spinWheel(wheelState: Animatable<Float, AnimationVector1D>, amounts: Array<String>, targetIndex: Int): String {
    val numberOfSections = amounts.size
    val sectionAngle = 360f / numberOfSections

    // Calculate the target rotation based on the index
    val targetRotation = (numberOfSections - targetIndex) * sectionAngle
    val randomSpin = Random.nextInt(5, 10) * 360f

    wheelState.animateTo(
        targetValue = randomSpin + targetRotation,
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        )
    )

    delay(100)

    // Return the amount at the target index
    return amounts[targetIndex]
}




suspend fun spinWheel(
    wheelState: Animatable<Float, AnimationVector1D>,
    amounts: List<String>,
    targetIndex: Int
): String {
    val initialRotation = wheelState.value
    val sectionAngle = 360f / amounts.size

    val finalRotationTarget = (targetIndex * sectionAngle) + (sectionAngle / 2)

    val randomSpins = Random.nextInt(5, 10) * 360f
    val totalRotation = initialRotation + randomSpins + finalRotationTarget

    wheelState.animateTo(
        targetValue = totalRotation,
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutSlowInEasing
        )
    )
    delay(100)

    return amounts[targetIndex]
}



@Composable
fun CelebrationAnimation(selectedAmount: String) {

    val animatedScale = remember { Animatable(0f) }

    LaunchedEffect(selectedAmount) {
        animatedScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    Text(
        text = "🎉 Congratulations! You won $selectedAmount 🎉",
        modifier = Modifier
            .scale(animatedScale.value),
        style = MaterialTheme.typography.headlineSmall,
        color = Color.Green
    )
}