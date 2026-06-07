package com.mrapps.mycard.ui.screens.creditcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrapps.mycard.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardViewModel = koinViewModel(),
    cardId: Int? = null
) {
    val cards by viewModel.cards.collectAsState(initial = emptyList())
    
    var cardProvider by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expireDate by remember { mutableStateOf("") }
    var cardOwnerName by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(White800) }
    var isChromatic by remember { mutableStateOf(false) }

    LaunchedEffect(cardId, cards) {
        if (cardId != null) {
            val card = cards.find { it.id == cardId }
            if (card != null) {
                cardProvider = card.cardProvider
                cardNumber = card.cardNumber
                expireDate = card.expireDate
                cardOwnerName = card.cardOwnerName
                cvc = card.cvc
                selectedColor = card.accentColor
                isChromatic = card.isChromatic
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (cardId == null) "Add New Card" else "Edit Card") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                InspectableCard(
                    cardNumber = cardNumber,
                    cardProvider = cardProvider,
                    cardOwnerName = cardOwnerName,
                    expireDate = expireDate,
                    cvc = cvc,
                    accentColor = selectedColor,
                    isChromatic = isChromatic
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cardProvider,
                onValueChange = { cardProvider = it },
                label = { Text("Card Provider (e.g. Visa, Bank)") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 19) cardNumber = it },
                label = { Text("Card Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expireDate,
                    onValueChange = { if (it.length <= 5) expireDate = it },
                    label = { Text("Expiry (MM/YY)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = cvc,
                    onValueChange = { if (it.length <= 3) cvc = it },
                    label = { Text("CVC") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cardOwnerName,
                onValueChange = { cardOwnerName = it },
                label = { Text("Name on Card") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Choose Card Color",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Chromatic", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isChromatic,
                        onCheckedChange = { isChromatic = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isChromatic) {
                HuePicker(
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(brush = Brush.linearGradient(chromaticColors))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chromatic Mode Active", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val card = CardData(
                        id = cardId ?: 0,
                        cardNumber = cardNumber,
                        cardProvider = cardProvider,
                        cardOwnerName = cardOwnerName,
                        expireDate = expireDate,
                        cvc = cvc,
                        accentColor = selectedColor,
                        isChromatic = isChromatic,
                        title = cardProvider.ifEmpty { "My Card" }
                    )
                    if (cardId == null) {
                        viewModel.addCard(card)
                    } else {
                        viewModel.updateCard(card)
                    }
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save Card", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
