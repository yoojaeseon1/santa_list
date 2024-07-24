package com.android.santa_list

import android.util.Log
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactViewModel : ViewModel() {
    fun onClickMore(popup: PopupMenu, currentFragment: ContactListFragment) {
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(currentFragment.context)
        val gridLayoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(currentFragment.context, 4)

        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main_menu_option, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_list -> {
                    Log.d("🤔 ??", "리스트 뷰 선택")
                    currentFragment.recyclerView.layoutManager = linearLayoutManager
                }

                R.id.menu_item_grid -> {
                    Log.d("🤔 ??", "그리드 뷰 선택")
                    currentFragment.recyclerView.layoutManager = gridLayoutManager
                }

                else -> {
                    Log.d("🤔 ??", "뭐여?")
                }
            }
            false
        }

        popup.show()
    }
}