package com.android.santa_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.Present
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.PresentRecyclerViewBinding


class PresentListAdapter(var targetFragment: Fragment = Fragment()) : ListAdapter<Present, PresentListAdapter.PresentHolder>(object : DiffUtil.ItemCallback<Present>(){
    override fun areItemsTheSame(oldItem: Present, newItem: Present): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Present, newItem: Present): Boolean {
        return oldItem == newItem
    }
}) {


    interface ImageClick{
        fun onClick()
    }

    var imageClick: ImageClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresentHolder {
        val binding = PresentRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PresentHolder(binding)
    }



    override fun onBindViewHolder(holder: PresentHolder, position: Int) {
        try{
//            holder.binding.imagePresent.setImageResource(getItem(position).imageResource)


            Log.d("presentListAdapter", "${getItem(position)}")
            if(position == 0) {
                holder.binding.imagePresent.setImageResource(R.drawable.image_add_present)
                holder.binding.imagePresent.setOnClickListener {
                    imageClick?.onClick()
                }
            } else {
                holder.binding.imagePresent.setImageURI(getItem(position).imageUri.toUri())
            }

//            currentList.size
        } catch(e: Exception) {
            Log.e("PresentListAdapter", "Exception : ${e}")
        }
    }

    inner class PresentHolder(val binding: PresentRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imagePresent
    }

}