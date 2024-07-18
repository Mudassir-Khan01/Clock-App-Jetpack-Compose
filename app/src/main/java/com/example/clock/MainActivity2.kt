package com.example.clock

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar



@Composable
fun clockWithNeon(){
    val infiniteTransition= rememberInfiniteTransition()

    val waveAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasWidth.coerceAtMost(canvasHeight) / 3
            val centerX = size.width / 2
            val centerY = size.height / 2

            val colors = listOf(
                Color(0xFFFF00FF), // Magenta
                Color(0xFF00FFFF), // Cyan
                Color(0xFF00FF00), // Green
//                Color(0xFFFFFF00), // Yellow
                Color(0xFFFFA500), // Orange
                Color(0xFF0000FF)  // Blue
            )
            for (i in 0 until 360 step 8) {
                val angle = Math.toRadians((i + waveAnimation).toDouble())
                val x = (radius * Math.cos(angle)).toFloat() + centerX
                val y = (radius * Math.sin(angle)).toFloat() + centerY

                val colorIndex = (i / 72) % colors.size
                val waveFactor = 10f * Math.sin(Math.toRadians((i + waveAnimation).toDouble())).toFloat()

                drawCircle(
                    color = colors[colorIndex],
                    radius = 10f + waveFactor,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }

            // Draw clock boundary
            drawCircle(
                color = Color.Black,
                radius = radius + 10.dp.toPx(), // Adjust the boundary width
                center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                style = Stroke(width = 5.dp.toPx())
            )
        }
        CustomClock(modifier = Modifier.size(300.dp).align(Alignment.Center), waveAnimation)
    }
}
@Composable
fun CustomClock(modifier: Modifier = Modifier, waveAnimation: Float) {
    val colors = listOf(
        Color(0xFFFF00FF), // Magenta
        Color(0xFF00FFFF), // Cyan
        Color(0xFF00FF00), // Green
//        Color(0xFFFFFF00), // Yellow
        Color(0xFFFFA500), // Orange
        Color(0xFF0000FF)  // Blue
    )
    Canvas(modifier = modifier) {
        val currentTime = Calendar.getInstance()
        val hours = currentTime.get(Calendar.HOUR)
        val minutes = currentTime.get(Calendar.MINUTE)
        val seconds = currentTime.get(Calendar.SECOND)

        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = canvasWidth.coerceAtMost(canvasHeight) / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        for (i in 0 until 360 step 5) {
            val angle = Math.toRadians((i + waveAnimation).toDouble())
            val x = (radius * Math.cos(angle)).toFloat() + centerX
            val y = (radius * Math.sin(angle)).toFloat() + centerY

            val colorIndex = (i / 72) % colors.size
            val waveFactor = 10f * Math.sin(Math.toRadians((i + waveAnimation).toDouble())).toFloat()

            drawCircle(
                color = colors[colorIndex],
                radius = 10f + waveFactor,
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }

        val hourAngle = Math.toRadians((360 / 12 * hours - 90).toDouble())
        val hourX = (radius * 0.5 * Math.cos(hourAngle)).toFloat() + centerX
        val hourY = (radius * 0.5 * Math.sin(hourAngle)).toFloat() + centerY
        drawLine(
            color = Color.White,
            start = center,
            end = androidx.compose.ui.geometry.Offset(hourX, hourY),
            strokeWidth = 8.dp.toPx()
        )

        val minuteAngle = Math.toRadians((360 / 60 * minutes - 90).toDouble())
        val minuteX = (radius * 0.7 * Math.cos(minuteAngle)).toFloat() + center.x
        val minuteY = (radius * 0.7 * Math.sin(minuteAngle)).toFloat() + center.y
        drawLine(
            color = Color.White,
            start = center,
            end = androidx.compose.ui.geometry.Offset(minuteX, minuteY),
            strokeWidth = 6.dp.toPx()
        )

        val secondAngle = Math.toRadians((360 / 60 * seconds - 90).toDouble())
        val secondX = (radius * 0.8 * Math.cos(secondAngle)).toFloat() + center.x
        val secondY = (radius * 0.8 * Math.sin(secondAngle)).toFloat() + center.y
        drawLine(
            color = Color.Red,
            start = center,
            end = androidx.compose.ui.geometry.Offset(secondX, secondY),
            strokeWidth = 4.dp.toPx()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun prev2(){
    clockWithNeon()
}
