package com.mrapps.mycard.ui.screens.creditcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrapps.mycard.data.CardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : ViewModel() {
    val cards: StateFlow<List<CardData>> = repository.allCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCard(card: CardData) {
        viewModelScope.launch {
            repository.insertCard(card)
        }
    }

    fun deleteCard(cardId: Int) {
        viewModelScope.launch {
            repository.deleteCard(cardId)
        }
    }

    fun updateCard(card: CardData) {
        viewModelScope.launch {
            repository.insertCard(card)
        }
    }
}
