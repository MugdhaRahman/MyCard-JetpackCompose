package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AccountScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        Text(text = "Account Screen")
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountScreenPreview() {
    AccountScreen()
}