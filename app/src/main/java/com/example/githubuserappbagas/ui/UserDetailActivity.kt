package com.example.githubuserappbagas.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserappbagas.R
import com.example.githubuserappbagas.data.adapter.SectionsPagerAdapter
import com.example.githubuserappbagas.data.response.DetailUserResponse
import com.example.githubuserappbagas.databinding.ActivityUserDetailBinding
import com.example.githubuserappbagas.model.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = "Detail User"

        val login = intent.getStringExtra(EXTRA_USER)

        if (login != null) {
            viewModel.detailUser(login)
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USER).toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getUserDetail.observe(this) { userProf ->
            showViewModel(userProf)

        }

        viewModel.getIsLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showViewModel(detailUser: DetailUserResponse) {

        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(binding.imgAvatar)

        binding.tvName.text = detailUser.name
        binding.tvUsername.text = detailUser.login
        binding.tvFollowersValue.text = "${detailUser.followers} Follower"
        binding.tvFollowingValue.text = "${detailUser.following} Following"

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}