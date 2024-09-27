package com.example.foodapp

import android.graphics.Paint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

    var amounts = remember { List(8) { Random.nextInt(1, 100).toString() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spinning Wheel", style = MaterialTheme.typography.headlineSmall) }
            )
        },
        content = {
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
                        painter = painterResource(id = R.drawable.arrow_pic),
                        contentDescription = "Arrow",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                            .rotate(140f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))


                Button(
                    onClick = {

                        showCelebration= false
                        amounts = List(8) { Random.nextInt(1, 100).toString() }

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
fun SpinningWheel(rotation: Float, amounts: List<String>) {
    val numberOfSections = amounts.size
    val colors = listOf(
        Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta, Color.Gray, Color.Black
    )
    val sectionAngle = 360f / numberOfSections

    Canvas(modifier = Modifier
        .size(300.dp)
        .clip(CircleShape)) {

        for (i in 0 until numberOfSections) {
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