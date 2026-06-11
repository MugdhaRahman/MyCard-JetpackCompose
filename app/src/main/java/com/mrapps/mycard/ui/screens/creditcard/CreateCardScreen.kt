package com.mrapps.mycard.ui.screens.creditcard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mrapps.mycard.ui.theme.White800
import org.koin.compose.viewmodel.koinViewModel
import java.util.Calendar

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
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        errorBorderColor = MaterialTheme.colorScheme.error,
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
            label = { Text("Card Provider (e.g. Visa, Bank)") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
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
            supportingText = if (cardNumberError) { { Text("Required") } } else null
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
                    supportingText = if (expireDateError) { { Text("Required") } } else null
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(4.dp))
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
                supportingText = if (cvcError) { { Text("Required") } } else null
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
            supportingText = if (cardOwnerNameError) { { Text("Required") } } else null
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
                Text(
                    "Chromatic Mode Active",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Glassy Gradient Save Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 40.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                    )
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                )
                .clickable {
                    cardNumberError = cardNumber.isEmpty()
                    expireDateError = expireDate.isEmpty()
                    cvcError = cvc.isEmpty()
                    cardOwnerNameError = cardOwnerName.isEmpty()

                    if (cardNumberError || expireDateError || cvcError || cardOwnerNameError) {
                        return@clickable
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
                    if (cardId == null) {
                        viewModel.addCard(card)
                    } else {
                        viewModel.updateCard(card)
                    }
                    onNavigateBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Save Card",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
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
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Expiry Date",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Month Selection
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(months) { month ->
                            val isSelected = selectedMonth == month
                            Text(
                                text = month,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedMonth = month }
                                    .padding(vertical = 8.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    ),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Year Selection
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(years) { year ->
                            val isSelected = selectedYear == year
                            Text(
                                text = "20$year",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedYear = year }
                                    .padding(vertical = 8.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(0.8f)
                                        else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    ),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text ="Cancel",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onDateSelected(selectedMonth, selectedYear) }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
