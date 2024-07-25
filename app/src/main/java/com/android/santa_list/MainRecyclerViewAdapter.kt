package com.android.santa_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserGridBinding
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(viewType: String) {
    LINEAR("ONE_LINE_TEXT"),
    GRID("TWO_LINE_TEXT"),
}

class MainRecyclerViewAdapter(private val contact: MutableList<User>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<ViewHolder>(){
    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    var itemClick : ItemClick? = null

    inner class LinearViewHolder(private val binding: ItemUserListBinding): ViewHolder(binding.root) {
        private val image = binding.ivItemImage
        private val name = binding.tvItemName
        private val isStarred = binding.ivItemStar

        fun bind(position: Int) {
            val contact = contact[position]

            image.setImageResource(contact.profile_image)
            name.text = contact.name
            isStarred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)

            isStarred.setOnClickListener {
                contact.is_starred = !contact.is_starred
                notifyItemChanged(position)
            }
        }
    }

    inner class GridViewHolder(private val binding: ItemUserGridBinding): ViewHolder(binding.root) {
        private val image = binding.ivItemImage
        private val name = binding.tvItemName
        private val isStarred = binding.ivItemStar

        fun bind(position: Int) {
            val contact = contact[position]

            image.setImageResource(contact.profile_image)
            name.text = contact.name
            isStarred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)

            isStarred.setOnClickListener {
                contact.is_starred = !contact.is_starred
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
        return when (recyclerView.layoutManager) {
            is GridLayoutManager -> CommonViewType.GRID.ordinal
            is LinearLayoutManager -> CommonViewType.LINEAR.ordinal
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        when (holder) {
            is LinearViewHolder -> holder.bind(position)
            is GridViewHolder -> holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}