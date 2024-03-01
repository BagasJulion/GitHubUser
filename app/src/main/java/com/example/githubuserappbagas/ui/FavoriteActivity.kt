package com.example.githubuserappbagas.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserappbagas.data.adapter.ListFavoriteAdapter
import com.example.githubuserappbagas.databinding.ActivityFavoriteBinding
import com.example.githubuserappbagas.model.UserViewModel
import com.example.githubuserappbagas.model.ViewModelFactory


class FavoriteActivity : AppCompatActivity(){

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val UserViewModel: UserViewModel by viewModels {
            factory
        }
        val userAdapter = ListFavoriteAdapter { user  ->
            if (user.isFavorite) {
                UserViewModel.deleteFavorite(user)
                Toast.makeText(this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show()
            } else {
                UserViewModel.saveFavorite(user)
                Toast.makeText(this, "Berhasil Menambah", Toast.LENGTH_SHORT).show()
            }
        }

        /*Get Data from API*/
        UserViewModel.getFavoriteUser().observe(this) { favoritedList ->
            userAdapter.submitList(favoritedList)
        }
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }
    }
}