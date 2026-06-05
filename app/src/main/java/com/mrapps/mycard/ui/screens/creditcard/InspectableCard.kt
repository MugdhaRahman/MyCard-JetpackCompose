package com.mrapps.mycard.ui.screens.creditcard

import androidx.annotation.DrawableRes
import com.mrapps.mycard.R
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrapps.mycard.ui.theme.Cyan100
import com.mrapps.mycard.ui.theme.Magenta100
import com.mrapps.mycard.ui.theme.Pink100
import com.mrapps.mycard.ui.theme.Yellow100
import androidx.compose.ui.tooling.preview.Preview
import com.mrapps.mycard.ui.theme.MyCardTheme
import com.mrapps.mycard.ui.theme.White800
import kotlin.math.cos
import kotlin.math.sin

val chromaticColors = listOf(
    Magenta100.copy(alpha = 0.12f),
    Cyan100.copy(alpha = 0.12f),
    Yellow100.copy(alpha = 0.12f),
    Pink100.copy(alpha = 0.12f),
    Color.Transparent
)

val partialChromaticColors = listOf(
    Cyan100.copy(alpha = 0.12f),
    Color.Transparent,
    Magenta100.copy(alpha = 0.12f)
)

@Composable
fun InspectableCard(
    modifier: Modifier = Modifier,
    @DrawableRes cardFrontDrawable: Int = R.drawable.mask_visa_front,
    @DrawableRes cardBackDrawable: Int = R.drawable.mask_visa_back,
    cardNumber: String = "",
    cardProvider: String = "",
    cardOwnerName: String = "",
    expireDate: String = "",
    cvc: String = "",
    accentColor: Color,
    isChromatic: Boolean = false,
    initialIsFlipped: Boolean = false
) {
    val localDensity = LocalDensity.current

    var isFlipped by rememberSaveable { mutableStateOf(initialIsFlipped) }
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    var cardWidth by remember { mutableFloatStateOf(0f) }
    var cardHeight by remember { mutableFloatStateOf(0f) }

    var touchX by remember { mutableFloatStateOf(0.5f) }
    var touchY by remember { mutableFloatStateOf(0.5f) }

    val flipRotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "flipRotation"
    )

    val animatedRotationX by animateFloatAsState(
        targetValue = rotationX, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationX"
    )

    val animatedRotationY by animateFloatAsState(
        targetValue = rotationY, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationY"
    )

    val animatedTouchX by animateFloatAsState(
        targetValue = touchX, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow
        ), label = "touchX"
    )

    val animatedTouchY by animateFloatAsState(
        targetValue = touchY, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow
        ), label = "touchY"
    )

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .onGloballyPositioned { coordinates ->
                    cardWidth = coordinates.size.width.toFloat()
                    cardHeight = coordinates.size.height.toFloat()
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            rotationX = 0f
                            rotationY = 0f
                            touchX = 0.5f
                            touchY = 0.5f
                        },
                        onDragCancel = {
                            rotationX = 0f
                            rotationY = 0f
                            touchX = 0.5f
                            touchY = 0.5f
                        },
                    ) { change, dragAmount ->

                        change.consume()

                        val currentTouchX = change.position.x
                        val currentTouchY = change.position.y

                        val normalizedX = (currentTouchX - cardWidth / 2f) / (cardWidth / 2f)
                        val normalizedY = (currentTouchY - cardHeight / 2f) / (cardHeight / 2f)

                        val maxRotation = 25f

                        rotationX = -normalizedY * maxRotation
                        rotationY = normalizedX * maxRotation

                        touchX = (currentTouchX / cardWidth).coerceIn(0f, 1f)
                        touchY = (currentTouchY / cardHeight).coerceIn(0f, 1f)
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            isFlipped = !isFlipped
                        })
                }
                .graphicsLayer {
                    this.rotationY = flipRotation + animatedRotationY
                    this.rotationX = animatedRotationX
                    cameraDistance = 12f * localDensity.density
                }, contentAlignment = Alignment.Center
        ) {
            CardFace(
                modifier = Modifier.wrapContentSize().run {
                    if (flipRotation > 90f) this.graphicsLayer { this.rotationY = 180f }
                    else this
                },
                cardDrawable = if (flipRotation <= 90f) cardFrontDrawable else cardBackDrawable,
                cardNumber = cardNumber,
                cardProvider = cardProvider,
                cardOwnerName = cardOwnerName,
                expireDate = expireDate,
                cvc = cvc,
                isFlipped = flipRotation > 90f,
                background = accentColor,
                isChromatic = isChromatic,
                touchX = animatedTouchX,
                touchY = animatedTouchY
            )
        }
    }
}

@Composable
fun CopyButton(text: String, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    IconButton(
        onClick = { clipboardManager.setText(AnnotatedString(text)) },
        modifier = modifier.size(32.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            modifier = Modifier.size(16.dp),
            tint = Color.Black.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun CardFace(
    modifier: Modifier = Modifier,
    @DrawableRes cardDrawable: Int,
    cardNumber: String = "",
    cardProvider: String = "",
    cardOwnerName: String = "",
    expireDate: String = "",
    cvc: String = "",
    isFlipped: Boolean = false,
    background: Color = Color.White,
    isChromatic: Boolean = false,
    touchX: Float = 0.5f,
    touchY: Float = 0.5f
) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.clip(shape = RoundedCornerShape(size = 20.dp)),
            colors = CardDefaults.cardColors(containerColor = if (isChromatic) Color.White else background),
        ) {
            Image(
                painter = painterResource(cardDrawable),
                modifier = Modifier.aspectRatio(0.66f),
                contentDescription = ""
            )
        }

        if (isFlipped) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape = RoundedCornerShape(size = 20.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 30.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CVC",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = cvc,
                            color = Color.Black,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        CopyButton(cvc)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape = RoundedCornerShape(size = 20.dp))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        if (cardProvider.isNotEmpty()) {
                            Text(
                                text = cardProvider,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 20.dp),
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = cardNumber,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                CopyButton(cardNumber)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.padding(end = 16.dp),
                                        text = "VALID THRU",
                                        color = Color.Black.copy(alpha = 0.6f),
                                        style = MaterialTheme.typography.labelSmall,
                                    )

                                    Text(
                                        text = expireDate,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                CopyButton(expireDate)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cardOwnerName,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                CopyButton(cardOwnerName)
                            }
                        }
                    }
                }
            }
        }

        // Chromatic overlay
        if (isChromatic) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape = RoundedCornerShape(size = 20.dp))
            ) {
                val gradientBrush = Brush.linearGradient(
                    colors = chromaticColors,
                    start = Offset.Zero,
                    end = Offset(x = size.width * 0.5f, y = size.height * 0.5f)
                )
                drawRect(
                    brush = gradientBrush, size = size, blendMode = BlendMode.Multiply
                )

                // Add secondary linear gradient for more dynamic effect
                val angle = (touchX - 0.5f) * 3.14f * 2f
                val gradientBrush2 = Brush.linearGradient(
                    colors = partialChromaticColors, start = Offset(
                        x = size.width * touchX - cos(angle) * size.width * 0.5f,
                        y = size.height * touchY - sin(angle) * size.height * 0.5f
                    ), end = Offset(
                        x = size.width * touchX + cos(angle) * size.width * 0.5f,
                        y = size.height * touchY + sin(angle) * size.height * 0.5f
                    )
                )

                drawRect(
                    brush = gradientBrush2, size = size, blendMode = BlendMode.Multiply
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InspectableCardPreview() {
    MyCardTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            InspectableCard(
                cardNumber = "1234 5678 9012 3456",
                cardProvider = "BANK",
                cardOwnerName = "John Doe",
                expireDate = "12/28",
                cvc = "123",
                accentColor = White800
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InspectableCardChromaticPreview() {
    MyCardTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            InspectableCard(
                cardNumber = "4567 8901 2345 6789",
                cardProvider = "Visa",
                cardOwnerName = "Alex Johnson",
                expireDate = "03/29",
                cvc = "789",
                accentColor = Magenta100.copy(alpha = 0.12f),
                isChromatic = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InspectableCardFlippedPreview() {
    MyCardTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            InspectableCard(
                cardNumber = "1234 5678 9012 3456",
                cardProvider = "Visa",
                cardOwnerName = "John Doe",
                expireDate = "12/28",
                cvc = "123",
                accentColor = White800,
                initialIsFlipped = true
            )
        }
    }
}
