package com.mrapps.mycard.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.mrapps.mycard.navigation.NavRoute
import com.mrapps.mycard.ui.components.GlassFab
import com.mrapps.mycard.ui.components.GlassNavItem
import com.mrapps.mycard.ui.components.LiquidGlassNavBar
import com.mrapps.mycard.ui.screens.creditcard.CardViewModel
import com.mrapps.mycard.ui.screens.creditcard.CreateCardScreen
import com.mrapps.mycard.ui.theme.Black700
import com.mrapps.mycard.ui.theme.LiquidGlassContainer
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

    val backgroundBackdrop = rememberLayerBackdrop()
    val contentBackdrop = rememberLayerBackdrop()

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
    val isGlassBack = currentDestination?.hasRoute<NavRoute.Settings>() == true ||
            currentDestination?.hasRoute<NavRoute.CreateCard>() == true

    Box(modifier = Modifier.fillMaxSize()) {
        // Separate Background Layer (Captured independently)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Black700.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.background.copy(alpha = 1f),
                        ),
                        center = Offset.Zero,
                        radius = 2400f
                    )
                )
                .layerBackdrop(backgroundBackdrop)
        )

        // UI Layer (Not inside the background layer's hierarchy)
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                if (isGlassBack) {
                                    LiquidGlassContainer(
                                        shape = CircleShape,
                                        blurRadius = 10.dp,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.ArrowBack,
                                            contentDescription = "Back",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
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
                                LiquidGlassContainer(
                                    shape = CircleShape,
                                    blurRadius = 10.dp,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = "Settings",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                AdaptiveNavBar(
                    currentDestination = currentDestination,
                    backdrop = contentBackdrop,
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
                    .layerBackdrop(contentBackdrop)
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
                        onNavigateBack = { navController.popBackStack() },
                        backdrop = backgroundBackdrop
                    )
                }
                composable<NavRoute.Account> { AccountScreen(backdrop = backgroundBackdrop) }
                composable<NavRoute.Settings> {
                    SettingsScreen(
                        viewModel = viewModel,
                        onEditCard = { cardId ->
                            navController.navigate(NavRoute.CreateCard(cardId))
                        },
                        backdrop = backgroundBackdrop
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptiveNavBar(
    currentDestination: androidx.navigation.NavDestination?,
    backdrop: com.kyant.backdrop.Backdrop,
    onNavigate: (NavRoute) -> Unit,
    onAddClick: () -> Unit
) {
    LiquidGlassNavBar(backdrop = backdrop) {
        GlassNavItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = currentDestination?.hierarchy?.any { it.hasRoute<NavRoute.Home>() } == true,
            onClick = { onNavigate(NavRoute.Home) },
            backdrop = backdrop
        )

        GlassFab(
            onClick = onAddClick,
            backdrop = backdrop,
            size = 54.dp,
            iconSize = 28.dp
        )

        GlassNavItem(
            icon = Icons.Default.AccountCircle,
            label = "Account",
            selected = currentDestination?.hierarchy?.any { it.hasRoute<NavRoute.Account>() } == true,
            onClick = { onNavigate(NavRoute.Account) },
            backdrop = backdrop
        )
    }
}
