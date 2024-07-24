package com.android.santa_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.UserList
import com.android.santa_list.databinding.FragmentContactListBinding
import com.android.santa_list.databinding.ItemUserListBinding

class MainRecyclerViewAdapter (private val contact :MutableList<UserList>) : RecyclerView.Adapter<MainRecyclerViewAdapter.Holder>(){

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    inner class Holder(private val binding: ItemUserListBinding):
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val name = binding.tvItemName
        val is_starred = binding.ivItemStar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        val contact = contact[position]

        holder.image.setImageResource(contact.image)
        holder.name.text = contact.name
        holder.is_starred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)


        holder.is_starred.setOnClickListener {
            contact.is_starred = !contact.is_starred
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}