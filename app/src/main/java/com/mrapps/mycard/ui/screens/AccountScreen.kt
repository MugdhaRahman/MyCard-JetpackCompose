package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.Backdrop
import com.mrapps.mycard.ui.components.GlassButton
import com.mrapps.mycard.ui.components.GlassFab
import com.mrapps.mycard.ui.screens.account.AccountData
import com.mrapps.mycard.ui.screens.account.AccountType
import com.mrapps.mycard.ui.screens.account.AccountViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = koinViewModel(),
    backdrop: Backdrop? = null
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var accountToEdit by remember { mutableStateOf<AccountData?>(null) }

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
                        onEdit = { accountToEdit = account },
                        onDelete = { viewModel.deleteAccount(account.id) }
                    )
                }
            }
        }

        // Glass FAB
        GlassFab(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            size = 64.dp,
            iconSize = 32.dp,
            contentDescription = "Add Account",
            backdrop = backdrop
        )

        if (showAddDialog) {
            AccountDialog(
                onDismiss = { showAddDialog = false },
                onSave = { account ->
                    viewModel.addAccount(account)
                    showAddDialog = false
                },
                backdrop = backdrop
            )
        }

        accountToEdit?.let { account ->
            AccountDialog(
                account = account,
                onDismiss = { accountToEdit = null },
                onSave = { updatedAccount ->
                    viewModel.addAccount(updatedAccount)
                    accountToEdit = null
                },
                backdrop = backdrop
            )
        }
    }
}
@Composable
fun AccountItem(
    account: AccountData,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.06f)
        ),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProviderLogo(
                name = account.providerName,
                size = 36.dp,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.providerName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                val displayValue = if (account.type == AccountType.BANK) {
                    account.accountNumber
                } else {
                    account.phoneNumberOrEmail
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    displayValue?.let { number ->
                        Text(
                            text = number,
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 14.sp
                        )
                        IconButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(number))
                            },
                            modifier = Modifier.size(20.dp)
                                .padding(start = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.White.copy(alpha = 0.55f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            
            }
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White.copy(alpha = 0.60f)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private val logoColors = listOf(
    Color(0xFF7424F2),
    Color(0xFF00E5FF),
    Color(0xFFFF4081),
    Color(0xFF42DC8C),
    Color(0xFFFF6B9D),
    Color(0xFFFC7808),
    Color(0xFFB2FF59),
    Color(0xFF4CDBFF),
    Color(0xFFFFEB3B),
    Color(0xFFE040FB),
)

private fun colorForName(name: String): Color {
    val index = name.hashCode().ushr(1).absoluteValue % logoColors.size
    return logoColors[index]
}

@Composable
fun ProviderLogo(
    name: String,
    size: Dp = 36.dp,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    val letter = name.firstOrNull()?.uppercase() ?: "?"
    val bgColor = colorForName(name)

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
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
            tint = Color.White.copy(alpha = 0.15f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Accounts Yet",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your bank info.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.45f)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 48.dp)
        )
    }
}

@Composable
fun AccountDialog(
    account: AccountData? = null,
    onDismiss: () -> Unit,
    onSave: (AccountData) -> Unit,
    backdrop: Backdrop? = null
) {
    var type by remember { mutableStateOf(account?.type ?: AccountType.MOBILE_BANKING) }
    var providerName by remember { mutableStateOf(account?.providerName ?: "") }
    var accountHolderName by remember { mutableStateOf(account?.accountHolderName ?: "") }

    var phoneNumberOrEmail by remember { mutableStateOf(account?.phoneNumberOrEmail ?: "") }
    var username by remember { mutableStateOf(account?.username ?: "") }

    var accountNumber by remember { mutableStateOf(account?.accountNumber ?: "") }
    var routingNumber by remember { mutableStateOf(account?.routingNumber ?: "") }
    var branchName by remember { mutableStateOf(account?.branchName ?: "") }
    var otherInfo by remember { mutableStateOf(account?.otherInfo ?: "") }

    var providerNameError by remember { mutableStateOf(false) }
    var accountHolderNameError by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White.copy(alpha = 0.35f),
        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White.copy(alpha = 0.75f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.35f)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(30.dp),
            color = Color(0xFF161616).copy(alpha = 0.95f),
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (account == null) "Add Account" else "Edit Account",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.04f))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                if (type == AccountType.MOBILE_BANKING) Color.White.copy(
                                    alpha = 0.1f
                                ) else Color.Transparent
                            )
                            .clickable { type = AccountType.MOBILE_BANKING },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Mobile",
                            color = if (type == AccountType.MOBILE_BANKING) Color.White else Color.White.copy(
                                alpha = 0.4f
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (type == AccountType.BANK) Color.White.copy(alpha = 0.1f) else Color.Transparent)
                            .clickable { type = AccountType.BANK },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Bank",
                            color = if (type == AccountType.BANK) Color.White else Color.White.copy(
                                alpha = 0.4f
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = providerName,
                    onValueChange = {
                        providerName = it
                        providerNameError = false
                    },
                    label = { Text("Provider Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = providerNameError,
                    colors = textFieldColors,
                    shape = RoundedCornerShape(18.dp)
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
                    colors = textFieldColors,
                    shape = RoundedCornerShape(18.dp)
                )

                if (type == AccountType.MOBILE_BANKING) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = phoneNumberOrEmail,
                        onValueChange = { phoneNumberOrEmail = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = accountNumber,
                        onValueChange = { accountNumber = it },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Glass Button
                GlassButton(
                    onClick = {
                        if (providerName.isEmpty()) providerNameError = true
                        if (accountHolderName.isEmpty()) accountHolderNameError = true
                        if (providerNameError || accountHolderNameError) return@GlassButton

                        onSave(
                            AccountData(
                                id = account?.id ?: 0,
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
                    modifier = Modifier.fillMaxWidth(),
                    backdrop = backdrop
                ) {
                    Text(
                        if (account == null) "Add Account" else "Save Changes",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel", color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    }
}
