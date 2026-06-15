package com.example.uas_ppb.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uas_ppb.data.CoffeeRepository
import com.example.uas_ppb.data.Member
import com.example.uas_ppb.data.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoffeeViewModel(private val repository: CoffeeRepository) : ViewModel() {

    val allMembers: StateFlow<List<Member>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memberCount: StateFlow<Int> = repository.memberCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun getMember(id: Int): Flow<Member?> = repository.getMemberById(id)

    fun getTransactions(memberId: Int): Flow<List<Transaction>> = 
        repository.getTransactionsForMember(memberId)

    fun addMember(name: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.insertMember(Member(name = name, email = email, phone = phone))
        }
    }

    fun addTransaction(memberId: Int, amount: Double) {
        viewModelScope.launch {
            repository.addTransaction(memberId, amount)
        }
    }

    fun redeemReward(memberId: Int, rewardName: String, points: Int) {
        viewModelScope.launch {
            repository.redeemReward(memberId, rewardName, points)
        }
    }
}

class CoffeeViewModelFactory(private val repository: CoffeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
