package com.intsoftdev.userpostsdemoapp.ui.postdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intsoftdev.domain.PostComments
import com.intsoftdev.userpostsdemoapp.databinding.PostDetailsItemBinding

class PostDetailsAdapter(
    private val comments: ArrayList<PostComments>
) : RecyclerView.Adapter<PostDetailsAdapter.CommentsViewHolder>() {

    inner class CommentsViewHolder(private val itemBinding: PostDetailsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(comment: PostComments) {
            itemBinding.postAuthor.text = comment.email
            itemBinding.postDescription.text = comment.name
            itemBinding.postComments.text = comment.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val itemBinding =
            PostDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    fun addComments(comments: List<PostComments>) {
        this.comments.apply {
            clear()
            addAll(comments)
        }
    }
}