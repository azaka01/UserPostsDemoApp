package com.intsoftdev.userpostsdemoapp.ui.userposts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intsoftdev.domain.PostModel
import com.intsoftdev.userpostsdemoapp.databinding.PostsRowLayoutBinding

class UserPostsAdapter(
    private val posts: ArrayList<PostModel>,
    private val listener: (PostModel) -> Unit
) :
    RecyclerView.Adapter<UserPostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val itemBinding: PostsRowLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(userPost: PostModel) {
            itemBinding.textPostTitle.text = userPost.title
            itemView.setOnClickListener {
                listener.invoke(userPost)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding =
            PostsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    fun addPosts(posts: List<PostModel>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
    }
}