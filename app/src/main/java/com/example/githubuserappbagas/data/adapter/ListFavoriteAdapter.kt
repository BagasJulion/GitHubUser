package com.example.githubuserappbagas.data.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserappbagas.R
import com.example.githubuserappbagas.database.UserEntity
import com.example.githubuserappbagas.databinding.ItemUserBinding
import com.example.githubuserappbagas.ui.UserDetailActivity


class ListFavoriteAdapter(private val onFavoriteClick : (UserEntity)->Unit) : ListAdapter<UserEntity, ListFavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: UserEntity) {
            Glide.with(itemView)
                .load(listUser.avatarUrl)
                .into(binding.imgProfile)
            binding.tvUser.text = listUser.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val favAdd = holder.binding.addFavorite
        if (user.isFavorite){
            favAdd.setImageDrawable(ContextCompat.getDrawable(favAdd.context, R.drawable.ic_favorite))
        }else{
            favAdd.setImageDrawable(ContextCompat.getDrawable(favAdd.context, R.drawable.baseline_favorite_border_24))
        }
        favAdd.setOnClickListener{

            onFavoriteClick(user)
        }

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, UserDetailActivity::class.java)
            intentDetail.putExtra(UserDetailActivity.EXTRA_USER, user.username)
            holder.itemView.context.startActivity(intentDetail)
            Log.d(TAG, "onBindViewHolder: ")
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }
        }

        private const val TAG = "ListFavoriteAdapter"
    }

}

