package com.mrapps.mycard.ui.screens.account

enum class AccountType {
    MOBILE_BANKING, BANK
}

data class AccountData(
    val id: Int = 0,
    val type: AccountType,
    val providerName: String,
    val accountHolderName: String,
    val phoneNumberOrEmail: String? = null,
    val username: String? = null,
    val accountNumber: String? = null,
    val routingNumber: String? = null,
    val branchName: String? = null,
    val otherInfo: String? = null
)
