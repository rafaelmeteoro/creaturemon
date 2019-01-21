package com.meteoro.creaturemon.view.avatars

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.meteoro.creaturemon.R
import com.meteoro.creaturemon.app.inflate
import com.meteoro.creaturemon.model.Avatar

class AvatarAdapter(private val avatars: List<Avatar>, private val listener: AvatarListener) :
    RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_avatar))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(avatars[position])
    }

    override fun getItemCount(): Int = avatars.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var avatar: Avatar

        private val imageView = itemView.findViewById<ImageView>(R.id.avatar)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(avatar: Avatar) {
            this.avatar = avatar
            val bitmap = BitmapFactory.decodeResource(imageView.context.resources, avatar.drawable)
            imageView.setImageDrawable(BitmapDrawable(imageView.context.resources, bitmap))
        }

        override fun onClick(v: View) {
            listener.avatarClicked(this.avatar)
        }
    }

    interface AvatarListener {
        fun avatarClicked(avatar: Avatar)
    }
}