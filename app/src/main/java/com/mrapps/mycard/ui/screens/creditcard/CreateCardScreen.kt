package com.mrapps.mycard.ui.screens.creditcard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kyant.backdrop.Backdrop
import com.mrapps.mycard.ui.components.GlassButton
import com.mrapps.mycard.ui.components.GlassSlider
import com.mrapps.mycard.ui.components.GlassSwitch
import com.mrapps.mycard.ui.theme.White800
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardScreen(
    onNavigateBack: () -> Unit,
    viewModel: CardViewModel = koinViewModel(),
    cardId: Int? = null,
    backdrop: Backdrop? = null
) {
    val cards by viewModel.cards.collectAsState(initial = emptyList())

    var cardProvider by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expireDate by remember { mutableStateOf("") }
    var cardOwnerName by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(White800) }
    var isChromatic by remember { mutableStateOf(false) }

    var cardNumberError by remember { mutableStateOf(false) }
    var expireDateError by remember { mutableStateOf(false) }
    var cardOwnerNameError by remember { mutableStateOf(false) }
    var cvcError by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

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

    if (showDatePicker) {
        MonthYearPickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { month, year ->
                expireDate = "$month/$year"
                expireDateError = false
                showDatePicker = false
            }
        )
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White.copy(alpha = 0.4f),
        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White.copy(alpha = 0.9f),
        cursorColor = Color.White,
        errorBorderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
        errorLabelColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            label = { Text("Card Provider") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { 
                if (it.length <= 19) {
                    cardNumber = it
                    cardNumberError = false
                }
            },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors,
            isError = cardNumberError,
            supportingText = if (cardNumberError) { { Text("Required") } } else null,
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = expireDate,
                    onValueChange = { },
                    label = { Text("Expiry (MM/YY)") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = textFieldColors,
                    isError = expireDateError,
                    supportingText = if (expireDateError) { { Text("Required") } } else null,
                    shape = RoundedCornerShape(18.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { showDatePicker = true }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = cvc,
                onValueChange = { 
                    if (it.length <= 3) {
                        cvc = it
                        cvcError = false
                    }
                },
                label = { Text("CVC") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors,
                isError = cvcError,
                supportingText = if (cvcError) { { Text("Required") } } else null,
                shape = RoundedCornerShape(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cardOwnerName,
            onValueChange = { 
                cardOwnerName = it
                cardOwnerNameError = false
            },
            label = { Text("Name on Card") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors,
            isError = cardOwnerNameError,
            supportingText = if (cardOwnerNameError) { { Text("Required") } } else null,
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Color Picker
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Card Theme Color",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            HuePicker(
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Glass Toggle for Chromatic Mode
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Chromatic Mode", color = Color.White, fontWeight = FontWeight.Bold)
            
            GlassSwitch(
                checked = isChromatic,
                onCheckedChange = { isChromatic = it },
                backdrop = backdrop
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Glass Button
        GlassButton(
            onClick = {
                cardNumberError = cardNumber.isEmpty()
                expireDateError = expireDate.isEmpty()
                cvcError = cvc.isEmpty()
                cardOwnerNameError = cardOwnerName.isEmpty()

                if (cardNumberError || expireDateError || cvcError || cardOwnerNameError) {
                    return@GlassButton
                }

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
                if (cardId == null) viewModel.addCard(card) else viewModel.updateCard(card)
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth(),
            backdrop = backdrop
        ) {
            Text("Save Card", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun MonthYearPickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit
) {
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear..currentYear + 20).map { it.toString().takeLast(2) }

    var selectedMonth by remember { mutableStateOf(months[Calendar.getInstance().get(Calendar.MONTH)]) }
    var selectedYear by remember { mutableStateOf(years[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF141414).copy(alpha = 0.98f),
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Expiry Date",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LazyColumn(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        items(months) { month ->
                            val isSelected = selectedMonth == month
                            Text(
                                text = month,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedMonth = month }
                                    .padding(vertical = 10.dp)
                                    .background(if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent, CircleShape),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.35f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        items(years) { year ->
                            val isSelected = selectedYear == year
                            Text(
                                text = "20$year",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedYear = year }
                                    .padding(vertical = 10.dp)
                                    .background(if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent, CircleShape),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = Color.White.copy(alpha = 0.35f)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .height(44.dp)
                            .padding(horizontal = 24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .border(0.5.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                            .clickable { onDateSelected(selectedMonth, selectedYear) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
