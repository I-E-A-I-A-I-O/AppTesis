package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.UsersDao
import com.justdance.apptesis.room.entities.Users

class UsersRepository(private val usersDao: UsersDao) {
    suspend fun getUsers(): List<Users> {
        return usersDao.getAll()
    }

    suspend fun isIdRegistered(id: String): Boolean {
        return usersDao.getById(id) != null
    }

    suspend fun insertUser(session: Users) {
        usersDao.insertAll(session)
    }

    suspend fun insertUser(session: ArrayList<Users>) {
        usersDao.insertAll(*session.toTypedArray())
    }

    suspend fun deleteUser(session: Users) {
        usersDao.delete(session)
    }
}