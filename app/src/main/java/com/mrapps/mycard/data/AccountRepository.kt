package com.mrapps.mycard.data

import com.mrapps.mycard.ui.screens.account.AccountData
import com.mrapps.mycard.ui.screens.account.AccountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepository(private val accountDao: AccountDao) {
    val allAccounts: Flow<List<AccountData>> = accountDao.getAllAccounts().map { entities ->
        entities.map { it.toAccountData() }
    }

    suspend fun insertAccount(account: AccountData) {
        accountDao.insertAccount(account.toEntity())
    }

    suspend fun deleteAccount(accountId: Int) {
        accountDao.deleteAccount(accountId)
    }

    private fun AccountEntity.toAccountData() = AccountData(
        id = id,
        type = AccountType.valueOf(type),
        providerName = providerName,
        accountHolderName = accountHolderName,
        phoneNumberOrEmail = phoneNumberOrEmail,
        username = username,
        accountNumber = accountNumber,
        routingNumber = routingNumber,
        branchName = branchName,
        otherInfo = otherInfo
    )

    private fun AccountData.toEntity() = AccountEntity(
        id = id,
        type = type.name,
        providerName = providerName,
        accountHolderName = accountHolderName,
        phoneNumberOrEmail = phoneNumberOrEmail,
        username = username,
        accountNumber = accountNumber,
        routingNumber = routingNumber,
        branchName = branchName,
        otherInfo = otherInfo
    )
}
