package com.example.githubuserappbagas.di

import android.content.Context
import com.example.githubuserappbagas.data.repo.UserRepository
import com.example.githubuserappbagas.database.UserDatabase
import com.example.githubuserappbagas.retrofit.ApiConfig
import com.example.githubuserappbagas.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}