package com.justdance.apptesis.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justdance.apptesis.room.entities.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM session")
    suspend fun getAll(): List<Session>

    @Insert
    suspend fun insertAll(vararg session: Session)

    @Delete
    suspend fun delete(session: Session)
}
