package com.intsoftdev.userpostsdemoapp.ui.userposts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.intsoftdev.domain.PostModel
import com.intsoftdev.domain.ResultState
import com.intsoftdev.userpostsdemoapp.databinding.FragmentUserPostsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserPostsFragment : Fragment(), (PostModel) -> Unit  {

    protected val userPostsViewModel: UserPostsViewModel by viewModel()
    private lateinit var userPostsAdapter: UserPostsAdapter

    private var _binding: FragmentUserPostsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView enter")
        _binding = FragmentUserPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated enter")
        setupObservers()
        setupUI()
        userPostsViewModel.getUserPosts()
    }

    private fun setupObservers() {
        userPostsViewModel.postsLiveData.observe(viewLifecycleOwner, { postResults ->
            when (postResults) {
                is ResultState.Success -> {
                    updateUI(postResults.data)
                }
                is ResultState.Failure -> {
                    hideLoadingStates()
                    Toast.makeText(
                        requireContext(),
                        "Unable to download data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun hideLoadingStates() {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.visibility = View.GONE
    }

    private fun updateUI(posts: List<PostModel>) {
        hideLoadingStates()
        binding.recyclerView.visibility = View.VISIBLE
        userPostsAdapter.apply {
            addPosts(posts)
        }
    }

    private fun setupUI() {
        userPostsAdapter  = UserPostsAdapter(arrayListOf(), this)
        with(binding.recyclerView) {
            this.layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            this.adapter = userPostsAdapter
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            userPostsViewModel.getUserPosts()
        }
    }

    override fun invoke(selectedPost: PostModel) {
        findNavController().navigate(
           UserPostsFragmentDirections.actionUserpostsToPostdetails(selectedPost.id)
        )
    }
}