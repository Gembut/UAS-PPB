package com.example.uas_ppb

import android.app.Application
import com.example.uas_ppb.data.AppDatabase
import com.example.uas_ppb.data.CoffeeRepository

class CoffeeBlissApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CoffeeRepository(database.memberDao(), database.transactionDao()) }
}
