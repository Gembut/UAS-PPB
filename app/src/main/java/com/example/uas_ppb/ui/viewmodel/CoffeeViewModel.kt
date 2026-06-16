package com.example.uas_ppb.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.uas_ppb.data.CoffeeRepository
import com.example.uas_ppb.data.Member
import com.example.uas_ppb.data.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class RegisterResult {
    object Success : RegisterResult()
    object MemberAlreadyExists : RegisterResult()
    object EmptyFields : RegisterResult()
}

sealed class LoginResult {
    data class Success(val memberId: Int) : LoginResult()
    object InvalidCredentials : LoginResult()
    object EmptyFields : LoginResult()
}

class CoffeeViewModel(
    private val repository: CoffeeRepository,
    context: Context
) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("coffee_bliss_prefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(sharedPreferences.getBoolean("is_logged_in", false))
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loggedInMemberId = MutableStateFlow<Int?>(
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            val id = sharedPreferences.getInt("logged_in_member_id", -1)
            if (id == -1) null else id
        } else null
    )
    val loggedInMemberId: StateFlow<Int?> = _loggedInMemberId.asStateFlow()

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

    fun updateMemberProfile(member: Member) {
        viewModelScope.launch {
            repository.updateMember(member)
        }
    }

    fun redeemReward(memberId: Int, rewardName: String, points: Int) {
        viewModelScope.launch {
            repository.redeemReward(memberId, rewardName, points)
        }
    }

    suspend fun register(name: String, email: String, phone: String, password: String): RegisterResult {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
            return RegisterResult.EmptyFields
        }
        val existingMember = repository.getMemberByEmailOrPhone(email, phone)
        if (existingMember != null) {
            return RegisterResult.MemberAlreadyExists
        }
        val member = Member(name = name, email = email, phone = phone, password = password)
        repository.registerMember(member)
        return RegisterResult.Success
    }

    suspend fun login(identifier: String, password: String): LoginResult {
        if (identifier.isBlank() || password.isBlank()) {
            return LoginResult.EmptyFields
        }
        val member = repository.getMemberByEmailOrPhone(identifier)
        if (member == null || member.password != password) {
            return LoginResult.InvalidCredentials
        }
        
        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", true)
            putInt("logged_in_member_id", member.id)
            apply()
        }
        _isLoggedIn.value = true
        _loggedInMemberId.value = member.id
        return LoginResult.Success(member.id)
    }

    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", false)
            putInt("logged_in_member_id", -1)
            apply()
        }
        _isLoggedIn.value = false
        _loggedInMemberId.value = null
    }
}

class CoffeeViewModelFactory(
    private val repository: CoffeeRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
