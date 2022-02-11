package com.justdance.apptesis.room.repository

import com.justdance.apptesis.room.dao.SessionDao
import com.justdance.apptesis.room.entities.Session

class SessionRepository(private val sessionDao: SessionDao) {
    suspend fun getSession(): List<Session> {
        return sessionDao.getAll()
    }

    suspend fun insertSession(session: Session) {
        sessionDao.insertAll(session)
    }

    suspend fun deleteSession(session: Session) {
        sessionDao.delete(session)
    }

    suspend fun getRole(): String {
        val roles = sessionDao.getRoles()
        return roles.first()
    }

    suspend fun getToken(): String {
        val tokens = sessionDao.getTokens()
        return tokens.first()
    }
}