package com.example.githubuserappbagas.model

import androidx.lifecycle.ViewModel
import com.example.githubuserappbagas.data.repo.UserRepository
import com.example.githubuserappbagas.database.UserEntity


class UserViewModel (private val userRepository: UserRepository) : ViewModel(){
    fun getAllUser(q: String= "") = userRepository.getAllUser(q)

    fun getFavoriteUser() = userRepository.getAllFavoriteUser()
    fun saveFavorite(favorite : UserEntity) {
        userRepository.setFavoriteUser(favorite,true)
    }
    fun deleteFavorite(favorite: UserEntity) {
        userRepository.setFavoriteUser(favorite, false)
    }
}