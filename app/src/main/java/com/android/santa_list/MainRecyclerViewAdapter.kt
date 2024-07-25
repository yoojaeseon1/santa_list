package com.android.santa_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserGridBinding
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(viewType: String) {
    LINEAR("ONE_LINE_TEXT"),
    GRID("TWO_LINE_TEXT"),
}
class MainRecyclerViewAdapter(private val contact: MutableList<User>, private val recyclerView: RecyclerView, private val listener: OnStarredChangeListener) : RecyclerView.Adapter<ViewHolder>(){

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    interface OnStarredChangeListener {
        fun onStarredChanged()
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
                listener.onStarredChanged()
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

    // TODO : Holder 다르게 적용할 수 있는 부분
//    override fun getItemViewType(position: Int): Int {
////        Log.d("테스트입니다~", "${}")
//        return if (position % 2 == 0) {
//            CommonViewType.LINEAR.ordinal
//        } else {
//            CommonViewType.GRID.ordinal
//        }
//    }

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