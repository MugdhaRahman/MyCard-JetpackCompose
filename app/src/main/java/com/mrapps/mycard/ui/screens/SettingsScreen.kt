package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.vibrancy
import com.mrapps.mycard.ui.screens.creditcard.CardData
import com.mrapps.mycard.ui.screens.creditcard.CardViewModel

@Composable
fun SettingsScreen(
    viewModel: CardViewModel,
    onEditCard: (Int) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: Backdrop? = null
) {
    val cards by viewModel.cards.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Cards",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        if (cards.isEmpty()) {
            Text(
                "No saved cards found.",
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cards) { card ->
                    CardItem(
                        card = card,
                        onEdit = { onEditCard(card.id) },
                        onDelete = { viewModel.deleteCard(card.id) },
                        backdrop = backdrop
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(
    card: CardData,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    backdrop: Backdrop? = null
) {
    val density = LocalDensity.current
    val glassModifier = if (backdrop != null) {
        Modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { RoundedCornerShape(24.dp) },
            effects = {
                vibrancy()
                blur(with(density) { 20.dp.toPx() })
            }
        )
    } else {
        Modifier.background(Color.White.copy(alpha = 0.08f)).blur(1.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .then(glassModifier)
            .background(Color.White.copy(alpha = 0.04f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.Transparent,
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.cardNumber.chunked(4).joinToString(" "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Card",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Card",
                        tint = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
