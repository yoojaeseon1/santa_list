package com.android.santa_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserGridBinding
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(viewType: String) {
    LINEAR("ONE_LINE_TEXT"),
    GRID("TWO_LINE_TEXT"),
}

class MainRecyclerViewAdapter(private val contact: MutableList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    var itemClick : ItemClick? = null


    inner class LinearViewHolder(private val binding: ItemUserListBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val name = binding.tvItemName
        val isStarred = binding.ivItemStar
    }

    inner class GridViewHolder(private val binding: ItemUserGridBinding): RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val name = binding.tvItemName
        val isStarred = binding.ivItemStar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val linearBinding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val gridBinding = ItemUserGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return when (viewType) {
            CommonViewType.LINEAR.ordinal -> {
                LinearViewHolder(linearBinding)
            }
            CommonViewType.GRID.ordinal -> {
                GridViewHolder(gridBinding)
            } else -> { LinearViewHolder(linearBinding) }
        }
    }

    override fun getItemViewType(position: Int): Int {
//        ContactViewModel().layout

//        Log.d("개 열받는다", "${ContactListFragment().recyclerView.layoutManager}")
        return if (position % 2 == 0) {
            CommonViewType.LINEAR.ordinal
        } else {
            CommonViewType.GRID.ordinal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        val contact = contact[position]

//        holder.image.setImageResource(contact.profile_image)
//        holder.name.text = contact.name
//        holder.isStarred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)
//
//        holder.isStarred.setOnClickListener {
//            contact.is_starred = !contact.is_starred
//            notifyItemChanged(position)
//        }
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}