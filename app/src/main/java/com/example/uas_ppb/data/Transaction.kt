package com.example.uas_ppb.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val title: String, // e.g., "Purchase" or "Redeem: Espresso"
    val amount: Double,
    val pointEarned: Int, // Positive for purchase, negative for redeem
    val date: String
) {
    val displayDate: String
        get() = formatTransactionDisplayDate(date)

    companion object {
        private const val STORAGE_PATTERN = "yyyy-MM-dd HH:mm:ss"
        private const val LEGACY_PATTERN = "dd MMM yyyy, HH:mm"
        private const val DISPLAY_PATTERN = "dd MMM yyyy, HH:mm"

        fun formatTransactionDisplayDate(value: String): String {
            val locale = Locale.getDefault()
            val displayFormatter = SimpleDateFormat(DISPLAY_PATTERN, locale)
            val parsedDate = listOf(STORAGE_PATTERN, LEGACY_PATTERN)
                .firstNotNullOfOrNull { pattern ->
                    runCatching {
                        SimpleDateFormat(pattern, locale).parse(value)
                    }.getOrNull()
                }

            return parsedDate?.let(displayFormatter::format) ?: value
        }
    }
}
