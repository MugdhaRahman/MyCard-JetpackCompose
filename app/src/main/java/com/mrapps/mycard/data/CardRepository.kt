package com.mrapps.mycard.data

import com.mrapps.mycard.ui.screens.creditcard.CardData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardRepository(private val cardDao: CardDao) {
    val allCards: Flow<List<CardData>> = cardDao.getAllCards().map { entities ->
        entities.map { it.toCardData() }
    }

    suspend fun insertCard(card: CardData) {
        cardDao.insertCard(card.toEntity())
    }

    suspend fun deleteCard(cardId: Int) {
        cardDao.deleteCard(cardId)
    }

    private fun CardEntity.toCardData() = CardData(
        id = id,
        cardNumber = cardNumber,
        cardProvider = cardProvider,
        cardOwnerName = cardOwnerName,
        expireDate = expireDate,
        cvc = cvc,
        accentColor = Color(accentColor),
        isChromatic = isChromatic,
        title = title,
        subtitle = subtitle
    )

    private fun CardData.toEntity() = CardEntity(
        id = id,
        title = title,
        subtitle = subtitle,
        cardNumber = cardNumber,
        cardProvider = cardProvider,
        cardOwnerName = cardOwnerName,
        expireDate = expireDate,
        cvc = cvc,
        accentColor = accentColor.toArgb(),
        isChromatic = isChromatic
    )
}
