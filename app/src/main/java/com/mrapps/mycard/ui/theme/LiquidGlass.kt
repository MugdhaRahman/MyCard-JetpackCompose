package com.mrapps.mycard.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A refined "Transparent Clear Liquid Glass" implementation.
 * Focuses on high transparency and blur for a premium glass effect.
 */
@Composable
fun LiquidGlassContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    blurRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.02f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            )
    ) {
        // High-blur layer for the "frost" effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurRadius)


        )
        
        // Content
        Box(
            modifier = Modifier.fillMaxSize()
                .border(
                    width = 0.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    ),
                    shape = shape
                ),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

// Clear Glass Modifier for quick application
val ClearLiquidGlassModifier = Modifier
    .clip(RoundedCornerShape(24.dp))
    .background(
        Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.1f), Color.White.copy(alpha = 0.05f))
        )
    )
    .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
