package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mrapps.mycard.navigation.NavRoute
import com.mrapps.mycard.ui.screens.creditcard.CardViewModel
import com.mrapps.mycard.ui.screens.creditcard.CreateCardScreen
import com.mrapps.mycard.ui.theme.Black700
import com.mrapps.mycard.ui.theme.Black900
import com.mrapps.mycard.ui.theme.White800
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: CardViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val cards by viewModel.cards.collectAsStateWithLifecycle()
    var currentAccentColor by remember { mutableStateOf(White800) }

    LaunchedEffect(cards) {
        if (cards.isNotEmpty() && currentDestination?.hasRoute<NavRoute.Home>() == true) {
            currentAccentColor = cards.first().accentColor
        }
    }

    val title = when {
        currentDestination?.hasRoute<NavRoute.Home>() == true -> "MyCard"
        currentDestination?.hasRoute<NavRoute.Account>() == true -> "Account"
        currentDestination?.hasRoute<NavRoute.Settings>() == true -> "Settings"
        currentDestination?.hasRoute<NavRoute.CreateCard>() == true -> {
            val route = navBackStackEntry?.toRoute<NavRoute.CreateCard>()
            if (route?.cardId == null) "Add New Card" else "Edit Card"
        }
        else -> "MyCard"
    }

    val showBackButton = currentDestination?.hasRoute<NavRoute.Home>() == false

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        currentAccentColor.copy(alpha = 0.4f),
                        Black700,
                        Black900,
                    ),
                    center = Offset.Zero,
                    radius = 2400f
                )
            )
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    actions = {
                        if (currentDestination?.hasRoute<NavRoute.Home>() == true) {
                            IconButton(onClick = {
                                navController.navigate(NavRoute.Settings) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        }
                    }
                )
            },
            bottomBar = {
                GlassyBottomNav(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onAddClick = {
                        navController.navigate(NavRoute.CreateCard())
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoute.Home,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable<NavRoute.Home> {
                    HomeScreen(
                        viewModel = viewModel,
                        onColorChange = { currentAccentColor = it }
                    )
                }
                composable<NavRoute.CreateCard> { backStackEntry ->
                    val route: NavRoute.CreateCard = backStackEntry.toRoute()
                    CreateCardScreen(
                        viewModel = viewModel,
                        cardId = route.cardId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable<NavRoute.Account> { AccountScreen() }
                composable<NavRoute.Settings> {
                    SettingsScreen(
                        viewModel = viewModel,
                        onEditCard = { cardId ->
                            navController.navigate(NavRoute.CreateCard(cardId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GlassyBottomNav(
    currentDestination: androidx.navigation.NavDestination?,
    onNavigate: (NavRoute) -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, end = 32.dp, bottom = 20.dp, start = 32.dp)
            .height(68.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                    )
                )
                .blur(4.dp)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GlassNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = currentDestination?.hierarchy?.any { it.hasRoute<NavRoute.Home>() } == true,
                onClick = { onNavigate(NavRoute.Home) }
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
                        ), CircleShape
                    )
                    .shadow(1.dp)


                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            GlassNavItem(
                icon = Icons.Default.AccountCircle,
                label = "Account",
                selected = currentDestination?.hierarchy?.any { it.hasRoute<NavRoute.Account>() } == true,
                onClick = { onNavigate(NavRoute.Account) }
            )
        }
    }
}

@Composable
fun GlassNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.4f
                )
            )
        }
    }
}
