package com.example.githubuserappbagas.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserappbagas.R
import com.example.githubuserappbagas.data.repo.Result
import com.example.githubuserappbagas.SettingPreferences
import com.example.githubuserappbagas.data.adapter.ListFavoriteAdapter
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.dataStore
import com.example.githubuserappbagas.databinding.ActivityMainBinding
import com.example.githubuserappbagas.model.MainViewModel
import com.example.githubuserappbagas.model.SettingViewModel
import com.example.githubuserappbagas.model.SettingViewModelFactory
import com.example.githubuserappbagas.model.UserViewModel
import com.example.githubuserappbagas.model.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.hide()
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val userViewModel: UserViewModel by viewModels {
            factory
        }

        val userAdapter = ListFavoriteAdapter { user ->
            if (user.isFavorite) {
                userViewModel.deleteFavorite(user)
                Toast.makeText(this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.saveFavorite(user)
                Toast.makeText(this, "Berhasil Menambah", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.getAllUser("Bagas").observe(this) { listUser ->
            if (listUser != null) {
                when (listUser) {
                    is Result.Loading -> {
                        mainViewModel.isLoading.observe(this){
                        showLoading(true)
                        }
                    }

                    is Result.Success -> {
                        mainViewModel.isLoading.observe(this){
                        val userData = listUser.data
                        userAdapter.submitList(userData)
                            showLoading(false)
                        }
                    }

                    is Result.Error -> {
                        showLoading(true)
                        Toast.makeText(
                            this,
                            "terjadi kesalahan" + listUser.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvListUser.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = userAdapter
        }

        with(binding) {
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.menu_settings -> {
                        val intent = Intent(this@MainActivity, ThemeActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, _, _ ->
                searchBar.text = searchView.text
                userViewModel.getAllUser(binding.searchView.text.toString())
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)
                searchView.hide()
                false
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}