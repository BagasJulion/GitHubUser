package com.example.githubuserappbagas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var INSTANCE:UserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) : UserDatabase {
            if (INSTANCE == null){
                synchronized(UserDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"user_database")
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }

}