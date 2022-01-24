package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(name = "user_email") val email: String,
    @ColumnInfo(name = "user_ci") val ci: String
)