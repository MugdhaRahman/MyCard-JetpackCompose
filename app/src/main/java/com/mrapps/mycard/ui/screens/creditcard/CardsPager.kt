package com.mrapps.mycard.ui.screens.creditcard

import android.annotation.SuppressLint
import com.mrapps.mycard.R
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrapps.mycard.ui.screens.HomeScreen
import com.mrapps.mycard.ui.theme.Black700
import com.mrapps.mycard.ui.theme.Black900
import com.mrapps.mycard.ui.theme.Blue200
import com.mrapps.mycard.ui.theme.Green300
import com.mrapps.mycard.ui.theme.Magenta100
import com.mrapps.mycard.ui.theme.MyCardTheme
import com.mrapps.mycard.ui.theme.TransparentGray100
import com.mrapps.mycard.ui.theme.TransparentWhite200
import com.mrapps.mycard.ui.theme.TransparentWhite600
import com.mrapps.mycard.ui.theme.White800
import kotlinx.coroutines.launch
import kotlin.math.abs

data class CardData(
    val title: String,
    val subtitle: String,
    val frontDrawable: Int,
    val backDrawable: Int,
    val accentColor: Color,
    val isChromatic: Boolean = false
)

@SuppressLint("ConfigurationScreenWidthHeight", "FrequentlyChangingValue")
@Composable
fun CardCollapsingPagerScreen(onColorChange: (Color) -> Unit = {}) {
    val cards = listOf(
        CardData(
            title = "Pure White",
            subtitle = "Clean. Minimal. All you need.",
            frontDrawable = R.drawable.mask_visa_front,
            backDrawable = R.drawable.mask_visa_back,
            accentColor = White800
        ), CardData(
            title = "Ocean Blue",
            subtitle = "Fresh energy in every transaction.",
            frontDrawable = R.drawable.mask_visa_front,
            backDrawable = R.drawable.mask_visa_back,
            accentColor = Blue200
        ), CardData(
            title = "Rainbow",
            subtitle = "Rare by design, yours by choice.",
            frontDrawable = R.drawable.mask_visa_front,
            backDrawable = R.drawable.mask_visa_back,
            accentColor = Magenta100.copy(alpha = 0.12f),
            isChromatic = true
        )
    )

    val pagerState = rememberPagerState(pageCount = { cards.size }, initialPage = 0)
    val currentPage = pagerState.settledPage
    val coroutineScope = rememberCoroutineScope()

    val animatedAccentColor by animateColorAsState(
        targetValue = cards[pagerState.currentPage].accentColor,
        animationSpec = tween(durationMillis = 600),
        label = "accentColor"
    )

    LaunchedEffect(animatedAccentColor) {
        onColorChange(animatedAccentColor)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                AnimatedContent(
                    targetState = currentPage, transitionSpec = {
                        slideInHorizontally(tween(500)) togetherWith fadeOut()
                    }, label = "Card Title Animation"
                ) { page ->
                    Text(
                        text = cards[page].title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold, color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedContent(
                    targetState = currentPage, transitionSpec = {
                        fadeIn(tween(700, delayMillis = 500)) togetherWith fadeOut()
                    }, label = "Card Subtitle Animation"
                ) { page ->
                    Text(
                        text = cards[page].subtitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White.copy(alpha = 0.7f), textAlign = TextAlign.Center
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            val configuration = LocalConfiguration.current

            val screenWidth = configuration.screenWidthDp.dp

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = screenWidth / 8),
                pageSpacing = 12.dp
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                val scale = animateFloatAsState(
                    targetValue = if (abs(pageOffset) < 0.5f) 1f else 0.85f,
                    animationSpec = tween(300),
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale.value),
                    contentAlignment = Alignment.Center
                ) {
                    InspectableCard(
                        modifier = Modifier.fillMaxHeight(),
                        cardFrontDrawable = cards[page].frontDrawable,
                        cardBackDrawable = cards[page].backDrawable,
                        accentColor = cards[page].accentColor,
                        isChromatic = cards[page].isChromatic
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Touch to explore",
                color = TransparentWhite600,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .animateContentSize(),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                cards.forEachIndexed { index, card ->
                    val isSelected = pagerState.currentPage == index
                    val scale = animateFloatAsState(
                        targetValue = if (isSelected) 1.2f else 1f,
                        animationSpec = tween(300),
                        label = "indicatorScale"
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .scale(scale.value)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }) {

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .border(
                                        width = 2.dp, color = Green300, shape = CircleShape
                                    )
                            )
                        }

                        Box(
                            modifier = Modifier.size(38.dp).align(Alignment.Center)
                                .clip(CircleShape).background(White800).run {
                                    if (card.isChromatic) background(
                                        brush = Brush.linearGradient(
                                            chromaticColors
                                        )
                                    )
                                    else background(color = card.accentColor.copy(alpha = if (isSelected) 1f else 0.85f))
                                })
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun CardCollapsingPagerScreenPreview() {
    MyCardTheme {
        CardCollapsingPagerScreen()
    }
}