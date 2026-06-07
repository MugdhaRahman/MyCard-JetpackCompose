package com.mrapps.mycard.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val cardNumber: String,
    val cardProvider: String,
    val cardOwnerName: String,
    val expireDate: String,
    val cvc: String,
    val accentColor: Int,
    val isChromatic: Boolean
)

class Converters {
    @TypeConverter
    fun fromColor(color: Color): Int = color.toArgb()

    @TypeConverter
    fun toColor(colorInt: Int): Color = Color(colorInt)
}
