package com.mrapps.mycard.di

import androidx.room.Room
import com.mrapps.mycard.data.AppDatabase
import com.mrapps.mycard.data.CardRepository
import com.mrapps.mycard.ui.screens.creditcard.CardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "mycard_database"
        ).build()
    }

    single { get<AppDatabase>().cardDao() }
    single { CardRepository(get()) }

    viewModel { CardViewModel(get()) }
}
