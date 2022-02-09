package com.justdance.apptesis.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(name = "user_ci") val ci: String,
    @ColumnInfo(name = "user_mail") val email: String
)