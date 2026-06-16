package com.example.uas_ppb.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(member: Member): Long

    @Update
    suspend fun update(member: Member)

    @Query("UPDATE members SET points = points + :addedPoints WHERE id = :memberId")
    suspend fun addPoints(memberId: Int, addedPoints: Int)

    @Query("UPDATE members SET points = points - :deductedPoints WHERE id = :memberId")
    suspend fun deductPoints(memberId: Int, deductedPoints: Int)

    @Query("SELECT * FROM members WHERE name = :name LIMIT 1")
    suspend fun getMemberByName(name: String): Member?

    @Query("SELECT * FROM members WHERE email = :identifier OR phone = :identifier LIMIT 1")
    suspend fun getMemberByEmailOrPhone(identifier: String): Member?

    @Query("SELECT * FROM members WHERE email = :email OR phone = :phone LIMIT 1")
    suspend fun getMemberByEmailOrPhone(email: String, phone: String): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Query("SELECT * FROM members WHERE phone = :phone LIMIT 1")
    suspend fun getMemberByPhone(phone: String): Member?

    @Query("SELECT COUNT(*) FROM members")
    fun getMemberCount(): Flow<Int>
}
