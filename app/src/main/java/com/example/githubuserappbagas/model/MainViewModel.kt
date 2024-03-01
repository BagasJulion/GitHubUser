package com.example.githubuserappbagas.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserappbagas.data.response.GithubResponse
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser : LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        findUser("Bagas")
    }


    fun findUser(q : String ) {
        try {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getListUsers(q)
            client.enqueue(object : Callback<GithubResponse> {
                override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _listUser.value = responseBody.items
                        }
                    } else {
                        _isLoading.value = false
                        Log.d(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        } catch (e: Exception) {
            Log.d("Token e", e.toString())
        }

    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}