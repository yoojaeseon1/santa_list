package com.android.santa_list

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ItemUserGridBinding
import com.android.santa_list.databinding.ItemUserListBinding

enum class CommonViewType(val viewType: Int) {
    LINEAR(0),
    GRID(1),
}

class MainRecyclerViewAdapter(
    private val onClick: (User) -> Unit,
    val context: Context?,
    var contact: MutableList<User>,
    private val recyclerView: RecyclerView,
    private val listener: OnStarredChangeListener
) : RecyclerView.Adapter<ViewHolder>(){
    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    interface OnStarredChangeListener {
        fun onStarredChanged()
    }

    private val santaUtil = SantaUtil.getInstance()

    var itemClick : ItemClick? = null

    // inner class로 하면 메모리 누수가 발생할 수 있음 -> inner를 삭제하면 된다고...
    inner class LinearViewHolder(private val binding: ItemUserListBinding): ViewHolder(binding.root) {
        private val image = binding.ivItemImage
        private val name = binding.tvItemName
        private val isStarred = binding.ivItemStar

        fun bind(position: Int) {
            val contact = contact[position]

            image.setImageResource(contact.profile_image)
            name.text = contact.name
            isStarred.setImageResource(if (contact.is_starred) R.drawable.icon_star else R.drawable.icon_empt_star)

            name.setOnClickListener {
                onClick(contact)
            }

            isStarred.setOnClickListener {
                contact.is_starred = !contact.is_starred
                listener.onStarredChanged()
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

            name.setOnClickListener { onClick(contact) }

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
        val enums = CommonViewType.entries.find { it.viewType == viewType } //immnutable로 반환해주기 때문에 훨씬 좋음!

        // ordinal로 values에 직접 접근하면 array에 접근하는 거라서 확장성의 문제가 있고 성능에 좋지 못함
        return when (enums) {
            CommonViewType.LINEAR -> {
                LinearViewHolder(linearBinding)
            }
            CommonViewType.GRID -> {
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

                        Log.d("MainRecyclerViewAdapter", "start swiping position = ${position}")
                        notifyItemChanged(position)
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