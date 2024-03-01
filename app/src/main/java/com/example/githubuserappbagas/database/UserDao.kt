package com.example.githubuserappbagas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM favoriteUser ORDER BY username ASC")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM favoriteUser where favorite = 1")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @PrimaryKey(autoGenerate = false)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userEntity: List<UserEntity>)

    @Query("DELETE FROM favoriteUser WHERE favorite = 0")
    fun delete()

    @Update
    fun updateFavorite(userEntity: UserEntity)

    //ascanding = ASC
    @Query("SELECT * from favoriteUser ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favoriteUser WHERE username = :username AND favorite =1)")
    fun isUserFavorite(username: String):Boolean

}