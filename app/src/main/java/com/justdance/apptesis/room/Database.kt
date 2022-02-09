package com.justdance.apptesis.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.justdance.apptesis.room.dao.CoursesDao
import com.justdance.apptesis.room.dao.SemestersDao
import com.justdance.apptesis.room.dao.SessionDao
import com.justdance.apptesis.room.dao.UsersDao
import com.justdance.apptesis.room.entities.Courses
import com.justdance.apptesis.room.entities.Semesters
import com.justdance.apptesis.room.entities.Session
import com.justdance.apptesis.room.entities.Users

@Database(
    entities = [
        Users::class,
        Semesters::class,
        Courses::class,
        Session::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun usersDao(): UsersDao
    abstract fun semestersDao(): SemestersDao
    abstract fun coursesDao(): CoursesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}