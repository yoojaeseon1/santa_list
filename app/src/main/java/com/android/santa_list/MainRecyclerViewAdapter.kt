package com.android.santa_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(viewType: String) {
    LINEAR("ONE_LINE_TEXT"),
    GRID("TWO_LINE_TEXT"),
}

class MainRecyclerViewAdapter(private val contact: MutableList<User>) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.Holder>() {
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    inner class Holder(private val binding: ItemUserListBinding, viewType: CommonViewType) :
        RecyclerView.ViewHolder(binding.root) {


        val image = binding.ivItemImage
        val name = binding.tvItemName
        val isStarred = binding.ivItemStar
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    class LinearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        Log.d("viewType", "${viewType}")
        val binding =
            ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        when (viewType) {
            CommonViewType.LINEAR.ordinal -> {
                return Holder(binding, CommonViewType.LINEAR)
            }

            CommonViewType.GRID.ordinal -> {}
            else -> {}
        }
        return Holder(binding, CommonViewType.LINEAR)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        val contact = contact[position]

        holder.image.setImageResource(contact.profile_image)
        holder.name.text = contact.name
        holder.isStarred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)

        holder.isStarred.setOnClickListener {
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