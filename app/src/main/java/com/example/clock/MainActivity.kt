package com.example.clock

import android.graphics.RadialGradient
import android.graphics.Shader
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Calendar



@Composable
fun ClockView(navController: NavController) {

    val infiniteTransition = rememberInfiniteTransition()

    val animatedColor = infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedPosition = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedBeat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0.0f at 0 with LinearEasing // Start
                1.0f at 500 with FastOutLinearInEasing // Peak of beat
                0.0f at 1000 with FastOutLinearInEasing // Back to normal
                 // Rest
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val currentTime = Calendar.getInstance()
            val hours = currentTime.get(Calendar.HOUR)
            val minutes = currentTime.get(Calendar.MINUTE)
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasWidth.coerceAtMost(canvasHeight) / 2
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension / 2 - 20.dp.toPx()
            val beatAmplitude = 15.dp.toPx()
            val dynamicRadius = baseRadius + beatAmplitude * animatedBeat.value

            val lightAngle = animatedPosition.value
            val lightX =
                centerX + dynamicRadius * kotlin.math.cos(Math.toRadians(lightAngle.toDouble())).toFloat()
            val lightY =
                centerY + dynamicRadius * kotlin.math.sin(Math.toRadians(lightAngle.toDouble())).toFloat()

            drawIntoCanvas { canvas ->
                val ambientPaint = Paint().apply {
                    shader = RadialGradient(
                        lightX, lightY, dynamicRadius,
                        intArrayOf(
                            Color.Transparent.toArgb(),
                            android.graphics.Color.argb(1, 255, 255, 255)
                        ),
                        floatArrayOf(0.85f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawCircle(Offset(centerX, centerY), dynamicRadius, ambientPaint)
            }

            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(animatedColor.value, Color.Green)
                ),
                center = Offset(centerX, centerY),
                radius = dynamicRadius,
                style = Stroke(width = 10.dp.toPx())
            )

            val hourAngle = Math.toRadians((360 / 12 * hours - 90).toDouble())
            val hourX = (radius * 0.5 * Math.cos(hourAngle)).toFloat() + centerX
            val hourY = (radius * 0.5 * Math.sin(hourAngle)).toFloat() + centerY
            drawLine(
                color = Color.Red,
                start = center,
                end = androidx.compose.ui.geometry.Offset(hourX, hourY),
                strokeWidth = 8.dp.toPx()
            )

            val minuteAngle = Math.toRadians((360 / 60 * minutes - 90).toDouble())
            val minuteX = (radius * 0.7 * Math.cos(minuteAngle)).toFloat() + center.x
            val minuteY = (radius * 0.7 * Math.sin(minuteAngle)).toFloat() + center.y
            drawLine(
                color = Color.Green,
                start = center,
                end = androidx.compose.ui.geometry.Offset(minuteX, minuteY),
                strokeWidth = 8.dp.toPx()
            )

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = {
               navController.navigate("clock2")
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text(text = "Switch", color = Color.Black)
            }
        }

    }


}



//@Preview(showBackground = true)
//@Composable
//fun ClockViewPreview() {
//    MaterialTheme {
//        ClockView()
//    }
//}


