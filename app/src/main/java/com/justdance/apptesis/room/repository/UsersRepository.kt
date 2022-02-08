package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.UsersDao
import com.justdance.apptesis.room.entities.Users

class UsersRepository(private val usersDao: UsersDao) {
    suspend fun getSession(): List<Users> {
        return usersDao.getAll()
    }

    suspend fun insertSession(session: Users) {
        usersDao.insertAll(session)
    }

    suspend fun deleteSession(session: Users) {
        usersDao.delete(session)
    }
}