package com.mrapps.mycard.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrapps.mycard.data.AccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {
    val accounts: StateFlow<List<AccountData>> = repository.allAccounts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addAccount(account: AccountData) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun deleteAccount(accountId: Int) {
        viewModelScope.launch {
            repository.deleteAccount(accountId)
        }
    }
}
