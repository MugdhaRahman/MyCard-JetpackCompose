package com.mrapps.mycard.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.kyant.backdrop.*
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import kotlinx.coroutines.launch

@Composable
fun LiquidGlassNavBar(
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null,
    content: @Composable RowScope.() -> Unit
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .height(72.dp)
    ) {
        val glassModifier = if (backdrop != null) {
            Modifier.drawBackdrop(
                backdrop = backdrop,
                shape = { RoundedCornerShape(32.dp) },
                effects = {
                    vibrancy()
                    blur(with(density) { 20.dp.toPx() })
                    lens(8f, 0.05f)
                }
            )
        } else {
            Modifier.background(Color.White.copy(alpha = 0.1f)).blur(1.dp)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .then(glassModifier)
                .background(Color.White.copy(alpha = 0.05f))
                .border(
                    width = 1.2.dp,
                    brush = Brush.sweepGradient(
                        listOf(
                            Color.Cyan.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.6f),
                            Color.Magenta.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content
        )
    }
}

@Composable
fun GlassFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String? = null,
    backdrop: Backdrop? = null,
    size: Dp = 56.dp,
    iconSize: Dp = 28.dp
) {
    val density = LocalDensity.current
    val animationScope = rememberCoroutineScope()
    val progressAnimation = remember { Animatable(0f) }

    val glassModifier = if (backdrop != null) {
        Modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { CircleShape },
            effects = {
                vibrancy()
                blur(with(density) { 15.dp.toPx() })
                lens(10f, 0.1f)
            },
            layerBlock = {
                val progress = progressAnimation.value
                val scale = lerp(1f, 1.15f, progress)
                scaleX = scale
                scaleY = scale
            }
        )
    } else {
        Modifier.background(Color.White.copy(alpha = 0.12f)).blur(1.dp)
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .then(glassModifier)
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.5.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Cyan.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.8f),
                        Color.Magenta.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.8f)
                    )
                ),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                val animationSpec = spring<Float>(0.5f, 300f, 0.001f)
                awaitEachGesture {
                    awaitFirstDown()
                    animationScope.launch { progressAnimation.animateTo(1f, animationSpec) }
                    waitForUpOrCancellation()
                    animationScope.launch { progressAnimation.animateTo(0f, animationSpec) }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun GlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val animationScope = rememberCoroutineScope()
    val progressAnimation = remember { Animatable(0f) }

    val glassModifier = if (backdrop != null) {
        Modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { CircleShape },
            effects = {
                vibrancy()
                blur(with(density) { 12.dp.toPx() })
                lens(6f, 0.05f)
            },
            layerBlock = {
                val progress = progressAnimation.value
                val scale = lerp(1f, 1.05f, progress)
                scaleX = scale
                scaleY = scale
            }
        )
    } else {
        Modifier.background(Color.White.copy(alpha = 0.1f)).blur(1.dp)
    }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(CircleShape)
            .then(glassModifier)
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.5.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Cyan.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.7f),
                        Color.Magenta.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.7f)
                    )
                ),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                val animationSpec = spring<Float>(0.5f, 300f, 0.001f)
                awaitEachGesture {
                    awaitFirstDown()
                    animationScope.launch { progressAnimation.animateTo(1f, animationSpec) }
                    waitForUpOrCancellation()
                    animationScope.launch { progressAnimation.animateTo(0f, animationSpec) }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun GlassNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    backdrop: Backdrop? = null
) {
    val density = LocalDensity.current
    val animationScope = rememberCoroutineScope()
    val progressAnimation = remember { Animatable(0f) }

    val glassModifier = if (backdrop != null) {
        Modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { CircleShape },
            effects = {
                if (selected) vibrancy()
                blur(with(density) { 8.dp.toPx() })
            },
            layerBlock = {
                val progress = progressAnimation.value
                val scale = lerp(1f, 1.1f, progress)
                scaleX = scale
                scaleY = scale
            }
        )
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .then(glassModifier)
            .pointerInput(Unit) {
                val animationSpec = spring<Float>(0.5f, 300f, 0.001f)
                awaitEachGesture {
                    awaitFirstDown()
                    animationScope.launch { progressAnimation.animateTo(1f, animationSpec) }
                    waitForUpOrCancellation()
                    animationScope.launch { progressAnimation.animateTo(0f, animationSpec) }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(
                    alpha = 0.5f
                )
            )
        }
    }
}

@Composable
fun GlassSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val trackBackdrop = rememberLayerBackdrop()
        val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

        // Slider Track
        Box(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .layerBackdrop(trackBackdrop)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(value)
                    .fillMaxHeight()
                    .background(trackColor, CircleShape)
            )
        }

        // Slider Thumb
        val thumbSize = 32.dp
        val maxOffset = maxWidth - thumbSize
        val thumbOffset = maxOffset * value

        val thumbBackdrop = if (backdrop != null) {
            rememberCombinedBackdrop(backdrop, trackBackdrop)
        } else {
            trackBackdrop
        }

        Box(
            Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .drawBackdrop(
                    backdrop = thumbBackdrop,
                    shape = { CircleShape },
                    effects = {
                        vibrancy()
                        lens(8f, 0.05f)
                    }
                )
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                .border(1.2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                .pointerInput(maxOffset) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        var currentPos = (value * maxOffset.toPx())
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.changes.any { it.pressed }) {
                                val change = event.changes.first()
                                currentPos += change.position.x - change.previousPosition.x
                                onValueChange((currentPos / maxOffset.toPx()).coerceIn(0f, 1f))
                                change.consume()
                            } else {
                                break
                            }
                        }
                    }
                }
        )
    }
}

@Composable
fun GlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    val density = LocalDensity.current
    val thumbOffset by animateDpAsState(targetValue = if (checked) 28.dp else 0.dp)
    
    Box(
        modifier = modifier
            .width(60.dp)
            .height(32.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f))
        )
        
        // Thumb (Lens)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(32.dp)
                .clip(CircleShape)
                .run {
                    if (backdrop != null) {
                        this.drawBackdrop(
                            backdrop = backdrop,
                            shape = { CircleShape },
                            effects = {
                                vibrancy()
                                lens(12f, 0.15f, chromaticAberration = true)
                            }
                        )
                    } else {
                        this.background(Color.White.copy(alpha = 0.15f)).blur(1.dp)
                    }
                }
                .background(Color.White.copy(alpha = 0.1f))
                .border(
                    width = 1.2.dp,
                    brush = Brush.sweepGradient(
                        listOf(
                            Color.Cyan.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.8f),
                            Color.Magenta.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.8f)
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}
