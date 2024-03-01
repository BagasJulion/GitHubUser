package com.example.githubuserappbagas.data.api


import com.example.githubuserappbagas.data.response.DetailUserResponse
import com.example.githubuserappbagas.data.response.GithubResponse
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.database.UserEntity
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("search/users")
    fun getListUsers(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUsers(@Path("username") login : String) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollower(@Path("username")followersUrl : String) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username")followingUrl : String) : Call<List<ItemsItem>>

}