package com.intsoftdev.userpostsdemoapp.ui.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.intsoftdev.domain.PostComments
import com.intsoftdev.domain.ResultState
import com.intsoftdev.userpostsdemoapp.R
import com.intsoftdev.userpostsdemoapp.databinding.FragmentPostDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailsFragment : Fragment() {

    private val args: PostDetailsFragmentArgs by navArgs()
    private lateinit var postDetailsAdapter: PostDetailsAdapter
    private val postDetailsViewModel: PostDetailsViewModel by viewModel()

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        getString(R.string.download_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setupUI() {
        postDetailsAdapter = PostDetailsAdapter(arrayListOf())
        with(binding.detailsRecyclerView) {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = postDetailsAdapter
        }
    }

    private fun updateUI(comments: List<PostComments>) {
        activity?.title = "Comments (${comments.size})"
        binding.detailsRecyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        postDetailsAdapter.apply {
            addComments(comments)
        }
    }
}