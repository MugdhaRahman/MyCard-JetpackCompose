package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrapps.mycard.ui.screens.account.AccountData
import com.mrapps.mycard.ui.screens.account.AccountType
import com.mrapps.mycard.ui.screens.account.AccountViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = koinViewModel()
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (accounts.isEmpty()) {
            EmptyAccountScreen()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(accounts) { account ->
                    AccountItem(
                        account = account,
                        onDelete = { viewModel.deleteAccount(account.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Account")
        }

        if (showAddDialog) {
            AddAccountDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { account ->
                    viewModel.addAccount(account)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AccountItem(
    account: AccountData,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (account.type == AccountType.BANK) Icons.Default.AccountBalance else Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.providerName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = account.accountHolderName,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                if (account.type == AccountType.BANK) {
                    Text(
                        text = "A/C: ${account.accountNumber}",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = account.phoneNumberOrEmail ?: "",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun EmptyAccountScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.White.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Accounts Yet",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your bank or mobile banking info.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    onAdd: (AccountData) -> Unit
) {
    var type by remember { mutableStateOf(AccountType.MOBILE_BANKING) }
    var providerName by remember { mutableStateOf("") }
    var accountHolderName by remember { mutableStateOf("") }
    
    // Mobile Banking
    var phoneNumberOrEmail by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    
    // Bank
    var accountNumber by remember { mutableStateOf("") }
    var routingNumber by remember { mutableStateOf("") }
    var branchName by remember { mutableStateOf("") }
    var otherInfo by remember { mutableStateOf("") }

    var providerNameError by remember { mutableStateOf(false) }
    var accountHolderNameError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Account",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TabRow(
                    selectedTabIndex = if (type == AccountType.MOBILE_BANKING) 0 else 1,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = type == AccountType.MOBILE_BANKING,
                        onClick = { type = AccountType.MOBILE_BANKING },
                        text = { Text("Mobile Banking") }
                    )
                    Tab(
                        selected = type == AccountType.BANK,
                        onClick = { type = AccountType.BANK },
                        text = { Text("Bank") }
                    )
                }

                OutlinedTextField(
                    value = providerName,
                    onValueChange = { 
                        providerName = it
                        providerNameError = false
                    },
                    label = { Text("Provider Name (e.g. bKash, Dutch Bangla)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = providerNameError,
                    supportingText = if (providerNameError) { { Text("Required") } } else null
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountHolderName,
                    onValueChange = { 
                        accountHolderName = it
                        accountHolderNameError = false
                    },
                    label = { Text("Account Holder Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = accountHolderNameError,
                    supportingText = if (accountHolderNameError) { { Text("Required") } } else null
                )

                if (type == AccountType.MOBILE_BANKING) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = phoneNumberOrEmail,
                        onValueChange = { phoneNumberOrEmail = it },
                        label = { Text("Phone Number or Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = routingNumber,
                        onValueChange = { routingNumber = it },
                        label = { Text("Routing Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = branchName,
                        onValueChange = { branchName = it },
                        label = { Text("Branch Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otherInfo,
                        onValueChange = { otherInfo = it },
                        label = { Text("Other Info (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (providerName.isEmpty()) {
                                providerNameError = true
                            }
                            if (accountHolderName.isEmpty()) {
                                accountHolderNameError = true
                            }
                            if (providerNameError || accountHolderNameError) return@Button

                            onAdd(
                                AccountData(
                                    type = type,
                                    providerName = providerName,
                                    accountHolderName = accountHolderName,
                                    phoneNumberOrEmail = phoneNumberOrEmail,
                                    username = username,
                                    accountNumber = accountNumber,
                                    routingNumber = routingNumber,
                                    branchName = branchName,
                                    otherInfo = otherInfo
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add Account")
                    }
                }
            }
        }
    }
}
