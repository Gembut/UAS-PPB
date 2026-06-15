package com.example.uas_ppb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val title: String, // e.g., "Purchase" or "Redeem: Espresso"
    val amount: Double,
    val pointEarned: Int, // Positive for purchase, negative for redeem
    val date: String
)
