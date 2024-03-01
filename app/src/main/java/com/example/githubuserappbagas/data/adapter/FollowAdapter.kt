package com.example.githubuserappbagas.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.databinding.ItemFollowBinding
import com.example.githubuserappbagas.ui.UserDetailActivity

class FollowAdapter : ListAdapter<ItemsItem, FollowAdapter.FollowMyViewHolder>(DIFF_CALLBACK) {


    class FollowMyViewHolder(private val binding: ItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: ItemsItem) {
            Glide.with(itemView)
                .load(listUser.avatarUrl)
                .into(binding.imgProfile)
            binding.tvUser.text = listUser.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowMyViewHolder {
        val binding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowMyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowMyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, UserDetailActivity::class.java)
            intentDetail.putExtra(UserDetailActivity.EXTRA_USER, user.login)
            it.context.startActivity(intentDetail)
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}