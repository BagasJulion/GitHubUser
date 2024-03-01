package com.example.githubuserappbagas.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserappbagas.data.adapter.FollowAdapter
import com.example.githubuserappbagas.data.response.ItemsItem
import com.example.githubuserappbagas.databinding.FragmentFollowBinding
import com.example.githubuserappbagas.model.DetailViewModel

class FollowersFragment : Fragment() {

    private lateinit var binding : FragmentFollowBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollow.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecor)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)

            if (position == 1) {
                detailViewModel.followers("$username")
                detailViewModel.getFollowers.observe(viewLifecycleOwner){listFollow ->
                    setUserFollow(listFollow)
                }
            } else {
                detailViewModel.following("$username")
                detailViewModel.getFollowing.observe(viewLifecycleOwner){listFollow ->
                    setUserFollow(listFollow)
                }

                detailViewModel.getIsLoading.observe(viewLifecycleOwner) { load ->
                    showLoading(load)
                }

            }
        }
    }

    private fun setUserFollow(listUser: List<ItemsItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(listUser)
        binding.rvFollow.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

}