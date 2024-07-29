package com.android.santa_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemIsStarredListBinding

class ContactIsStarredAdapter(private val contact: MutableList<User>) : RecyclerView.Adapter<ContactIsStarredAdapter.Holder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val binding = ItemIsStarredListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        val contact = contact[position]
        if(contact.profile_image >= 0)
            holder.image.setImageResource(contact.profile_image)
        else
            holder.image.setImageURI(contact.profile_image_uri.toUri())

        holder.name.text = contact.name

    }

    interface  ItemClick {
        fun onClick(view : View, position: Int)
    }

    private var itemClick : ItemClick? = null

    inner class Holder(private val binding: ItemIsStarredListBinding):
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemIsStarredImage
        val name = binding.tvItemIsStarredName
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}