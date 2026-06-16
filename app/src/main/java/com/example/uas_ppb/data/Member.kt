package com.example.uas_ppb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val password: String = "",
    val points: Int = 0
) {
    val memberId: String get() = "MBR${id.toString().padStart(5, '0')}"
    val level: String get() = when {
        points >= 500 -> "Gold"
        points >= 200 -> "Silver"
        else -> "Bronze"
    }
}
