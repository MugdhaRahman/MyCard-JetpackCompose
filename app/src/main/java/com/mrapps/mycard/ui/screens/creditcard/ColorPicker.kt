package com.mrapps.mycard.ui.screens.creditcard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.mrapps.mycard.ui.theme.LiquidGlassContainer

@Composable
fun HuePicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var width by remember { mutableFloatStateOf(0f) }
    
    val hsv = remember(selectedColor) {
        val floatArray = FloatArray(3)
        android.graphics.Color.colorToHSV(selectedColor.toArgb(), floatArray)
        floatArray
    }
    
    val hue = hsv[0]

    val rainbowBrush = remember {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
            )
        )
    }

    LiquidGlassContainer(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .onSizeChanged { width = it.width.toFloat() }
            .pointerInput(width) {
                detectTapGestures { offset ->
                    if (width > 0) {
                        val newHue = (offset.x / width).coerceIn(0f, 1f) * 360f
                        onColorSelected(Color.hsv(newHue, 1f, 1f))
                    }
                }
            }
            .pointerInput(width) {
                detectDragGestures { change, _ ->
                    if (width > 0) {
                        val newHue = (change.position.x / width).coerceIn(0f, 1f) * 360f
                        onColorSelected(Color.hsv(newHue, 1f, 1f))
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = rainbowBrush)

            val x = (hue / 360f) * width
            drawCircle(
                color = Color.White,
                radius = 15.dp.toPx(),
                center = Offset(x, size.height / 2),
                style = Stroke(width = 3.dp.toPx())
            )
            drawCircle(
                color = selectedColor,
                radius = 12.dp.toPx(),
                center = Offset(x, size.height / 2)
            )
        }
    }
}
