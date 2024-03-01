package com.example.githubuserappbagas.model

import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserappbagas.data.response.DetailUserResponse
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.database.UserEntity
import com.example.githubuserappbagas.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){
    private val userDetail = MutableLiveData<DetailUserResponse>()
    val getUserDetail: LiveData<DetailUserResponse> = userDetail

    private val followers =  MutableLiveData<List<ItemsItem>>()
    val getFollowers:  LiveData<List<ItemsItem>>  = followers

    private val following =  MutableLiveData<List<ItemsItem>>()
    val getFollowing:  LiveData<List<ItemsItem>>  = following

    private val isLoading = MutableLiveData<Boolean>()
    val getIsLoading: LiveData<Boolean> = isLoading

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun detailUser(username: String) {
        try {
            isLoading.value = true

            ApiConfig.getApiService()
                .getDetailUsers(username)
                .enqueue(object : Callback<DetailUserResponse> {
                    override fun onResponse(
                        call: Call<DetailUserResponse>,
                        response: Response<DetailUserResponse>
                    ) {
                        isLoading.value = false
                        if (response.isSuccessful) {
                            userDetail.value = response.body()
                        }
                    }

                    override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                        isLoading.value = false
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }

    }

    fun followers(username: String) {
        try {
            isLoading.value = true
            ApiConfig.getApiService()
                .getFollower(username)
                .enqueue(object : Callback<List<ItemsItem>> {
                    override fun onResponse(
                        call: Call<List<ItemsItem>>,
                        response: Response<List<ItemsItem>>
                    ) {
                        isLoading.value = false
                        if (response.isSuccessful && response.body() != null) {
                            followers.value = response.body()
                        }
                    }

                    override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                        isLoading.value = false
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }

    }

    fun following(username: String) {
        try {
            isLoading.value = true
            ApiConfig.getApiService()
                .getFollowing(username)
                .enqueue(object : Callback<List<ItemsItem>> {
                    override fun onResponse(
                        call: Call<List<ItemsItem>>,
                        response: Response<List<ItemsItem>>
                    ) {
                        isLoading.value = false
                        if (response.isSuccessful && response.body() != null) {
                            following.value = response.body()
                            Log.d("success",response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                        isLoading.value = false
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }

    }
}