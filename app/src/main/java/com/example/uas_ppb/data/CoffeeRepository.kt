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

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
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
}
