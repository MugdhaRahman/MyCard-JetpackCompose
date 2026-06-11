package com.mrapps.mycard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "MOBILE_BANKING" or "BANK"
    val providerName: String,
    val accountHolderName: String,
    // Mobile Banking specific
    val phoneNumberOrEmail: String? = null,
    val username: String? = null,
    // Bank specific
    val accountNumber: String? = null,
    val routingNumber: String? = null,
    val branchName: String? = null,
    val otherInfo: String? = null
)
