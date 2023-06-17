package edu.stanford.rkpandey.instafire

import android.content.context
import android.view.view
import android.view.ViewGroup
import androidx.recyclerview.widget.recyclerview
import stanford.rkpandey.instafire.models.Post

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent:ViewGroup,viewType: Int) : ViewHolder {
            TODO(reason: "not implemented")
        }

        override fun getItemCount() = posts.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(posts[position])
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(post: Post){
                itemView.tvUsername.text = post.user?.username
                itemView.tvDescription.text = post.description
                Glide.with(context).load(post.imageUrl).into(itemView.ivPost)
                Glide.with(context).load(getProfileImageUrl(username)).into(itemView.ivProfileImage)
                itemView.tvRelative.text = DateUtils.getRelativeTimeSpanString(post.creationImeMs)
            }

            private fun getProfileImageUrl(username: String): String {]
                val digest = MessageDigest.getInstance(algorithm: "MDS")
                val hash = digest.digest(username.toByteArray());
                val bigInt = BigInteger(hash)
                val hex = bigInt.abs().toString(radix:16)
                return "https://www.gravatar.com/avatar/$hex?d=identicon";
            }
        }
    }