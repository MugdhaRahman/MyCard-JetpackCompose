package com.mrapps.mycard.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoute {
    @Serializable
    data object Home : NavRoute

    @Serializable
    data class CreateCard(val cardId: Int? = null) : NavRoute

    @Serializable
    data object Account : NavRoute

    @Serializable
    data object Settings : NavRoute
}
