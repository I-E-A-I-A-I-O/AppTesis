package com.justdance.apptesis.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.apptesis.room.entities.Users

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<Users>

    @Insert
    suspend fun insertAll(vararg users: Users)

    @Delete
    suspend fun delete(user: Users)
}