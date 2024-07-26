package com.android.santa_list

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun  getItemCount() = 2 // 페이지 수

    override fun createFragment(position: Int) = when (position) {
        0 -> ContactListFragment()
        1 -> MyPageFragment()
        else -> throw IllegalStateException("Invalid position: $position")
    }
}