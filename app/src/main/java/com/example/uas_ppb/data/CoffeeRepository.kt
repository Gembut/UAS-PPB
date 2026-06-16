package com.example.uas_ppb.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CoffeeRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()
    val memberCount: Flow<Int> = memberDao.getMemberCount()

    fun getMemberById(id: Int): Flow<Member?> = memberDao.getMemberById(id)

    suspend fun insertMember(member: Member): Long = memberDao.insert(member)

    suspend fun updateMember(member: Member) = memberDao.update(member)

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    suspend fun addTransaction(memberId: Int, amount: Double) {
        val points = (amount / 10000).toInt()
        val transaction = Transaction(
            memberId = memberId,
            title = "Purchase",
            amount = amount,
            pointEarned = points,
            date = getCurrentDate()
        )
        transactionDao.insert(transaction)
        memberDao.addPoints(memberId, points)
    }

    fun getTransactionsForMember(memberId: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsForMember(memberId)

    suspend fun redeemReward(memberId: Int, rewardName: String, rewardPoints: Int) {
        val transaction = Transaction(
            memberId = memberId,
            title = "Redeem: $rewardName",
            amount = 0.0,
            pointEarned = -rewardPoints,
            date = getCurrentDate()
        )
        transactionDao.insert(transaction)
        memberDao.deductPoints(memberId, rewardPoints)
    }

    // Member authentication functions
    suspend fun registerMember(member: Member): Long = memberDao.insert(member)
    
    suspend fun getMemberByName(name: String): Member? = memberDao.getMemberByName(name)

    suspend fun getMemberByEmailOrPhone(identifier: String): Member? =
        memberDao.getMemberByEmailOrPhone(identifier)

    suspend fun getMemberByEmailOrPhone(email: String, phone: String): Member? =
        memberDao.getMemberByEmailOrPhone(email, phone)
}
