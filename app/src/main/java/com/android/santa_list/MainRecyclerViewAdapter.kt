package com.android.santa_list

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserGridBinding
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(viewType: String) {
    LINEAR("ONE_LINE_TEXT"),
    GRID("TWO_LINE_TEXT"),
}
class MainRecyclerViewAdapter(val context: Context?, private val contact: MutableList<User>, private val recyclerView: RecyclerView, private val listener: OnStarredChangeListener) : RecyclerView.Adapter<ViewHolder>(){
    private val santaUtil = SantaUtil.getInstance()

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

    override fun getItemViewType(position: Int): Int {
        return when (recyclerView.layoutManager) {
            is GridLayoutManager -> CommonViewType.GRID.ordinal
            is LinearLayoutManager -> CommonViewType.LINEAR.ordinal
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }

        when (holder) {
            is LinearViewHolder -> holder.bind(position)
            is GridViewHolder -> holder.bind(position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return false
            }


            // 오른쪽으로 스와이프 할 경우
            // TODO: 아래 코드 튜터님께 여쭤보기
            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val test = context as Activity

                if (direction == ItemTouchHelper.RIGHT) {
                    val position = viewHolder.adapterPosition
                    if (context.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE) } != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(test, arrayOf(Manifest.permission.CALL_PHONE), 1)
                    } else {
                        val phoneNumber = "tel:" + santaUtil.removePhoneHyphen(contact[position].phone_number)
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber))
                        test.startActivity(intent)
                    }
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


}