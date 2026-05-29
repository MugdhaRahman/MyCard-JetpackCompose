package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrapps.mycard.ui.theme.MyCardTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

    ) {
        Text("Home Screen")
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MyCardTheme {
        HomeScreen()
    }
}
