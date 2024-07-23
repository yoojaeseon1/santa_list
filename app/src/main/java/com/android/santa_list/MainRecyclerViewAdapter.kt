package com.android.santa_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.MyContact
import com.android.santa_list.databinding.FragmentContactListBinding
import com.android.santa_list.databinding.ItemFriendsListBinding

class MainRecyclerViewAdapter (private val contact :MutableList<MyContact>) : RecyclerView.Adapter<MainRecyclerViewAdapter.Holder>(){

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemFriendsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val contact = contact[position]

        holder.image.setImageResource(position)
        holder.name.text = contact.user.id
        holder.bookmark.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)


        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        holder.bookmark.setOnClickListener {
            contact.is_starred = !contact.is_starred
            notifyItemChanged(position)
        }
    }

    inner class Holder(val binding: ItemFriendsListBinding):
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val name = binding.tvItemName
        val bookmark = binding.ivItemStar
    }

    override fun getItemCount(): Int {
        return contact.size
    }


}