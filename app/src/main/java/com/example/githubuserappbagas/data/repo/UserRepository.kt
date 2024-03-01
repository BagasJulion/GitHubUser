package com.example.githubuserappbagas.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.githubuserappbagas.data.api.ApiService
import com.example.githubuserappbagas.data.response.GithubResponse
import com.example.githubuserappbagas.database.UserDao
import com.example.githubuserappbagas.database.UserEntity
import com.example.githubuserappbagas.retrofit.ApiConfig
import com.example.githubuserappbagas.utils.AppExecutors
import retrofit2.Response


class UserRepository private constructor(
    private val userDao : UserDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<UserEntity>>>()

    fun getAllUser(q : String ): LiveData<Result<List<UserEntity>>> {
        result.value = Result.Loading
        val client = ApiConfig.getApiService().getListUsers(q)
        client.enqueue(object : retrofit2.Callback<GithubResponse> {
            override fun onResponse(call: retrofit2.Call<GithubResponse>, response: Response<GithubResponse>) {

                result.value = Result.Loading
                if (response.isSuccessful) {
                    val items = response.body()?.items
                    val favoriteList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        items?.forEach { itemsItem ->
                            val isFavorite = userDao.isUserFavorite(itemsItem.login)
                            val favorite = UserEntity(
                                itemsItem.login,
                                itemsItem.avatarUrl,
                                isFavorite
                            )
                            favoriteList.add(favorite)
                        }
                        userDao.delete()
                        userDao.insert(favoriteList)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<GithubResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = userDao.getUser()
        result.addSource(localData){newData :List<UserEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getAllFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    fun setFavoriteUser(favorite: UserEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            favorite.isFavorite = bookmarkState
            userDao.updateFavorite(favorite)
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(favoriteDao, appExecutors)
            }.also { instance = it }
    }
}