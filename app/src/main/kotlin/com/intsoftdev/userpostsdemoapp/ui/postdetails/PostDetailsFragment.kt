package com.intsoftdev.userpostsdemoapp.ui.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.intsoftdev.domain.PostComments
import com.intsoftdev.domain.PostModel
import com.intsoftdev.domain.ResultState
import com.intsoftdev.userpostsdemoapp.databinding.FragmentPostDetailsBinding
import com.intsoftdev.userpostsdemoapp.ui.userposts.UserPostsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PostDetailsFragment : Fragment() {

    private val args: PostDetailsFragmentArgs by navArgs()
    private lateinit var postDetailsAdapter: PostDetailsAdapter
    protected val postDetailsViewModel: PostDetailsViewModel by viewModel()

    private var _binding: FragmentPostDetailsBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView enter")
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated enter")
        setupObservers()
        setupUI()
        postDetailsViewModel.getPostComments(args.postId)
    }

    private fun setupObservers() {
        postDetailsViewModel.commentsLiveData.observe(viewLifecycleOwner, { postResults ->
            when (postResults) {
                is ResultState.Success -> {
                    updateUI(postResults.data)
                }
                is ResultState.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Unable to download data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupUI() {
        postDetailsAdapter  = PostDetailsAdapter(arrayListOf())
        with(binding.detailsRecyclerView) {
            this.layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            this.adapter = postDetailsAdapter
        }
    }

    private fun updateUI(comments: List<PostComments>) {
        binding.detailsRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        postDetailsAdapter.apply {
            addComments(comments)
        }
    }
}