package com.android.santa_list

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.ActivityContactBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContactActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener, ChangeFragmentListener {
    private val binding: ActivityContactBinding by lazy { ActivityContactBinding.inflate( layoutInflater ) }
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var viewPager: ViewPager2
    private val tabTitles = arrayOf("연락처", "내정보")
    private val tabSelectedIcons = arrayOf(R.drawable.ic_phone_selected, R.drawable.ic_my_selected)
    private val tabUnselectedIcons = arrayOf(R.drawable.ic_phone_unselected, R.drawable.ic_my_unselected)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            tabLayout.addOnTabSelectedListener(this@ContactActivity) // 다형성을 추가한 것
            viewPager = pager
            viewPager.adapter = ViewPager2Adapter(this@ContactActivity)
            viewPager.offscreenPageLimit = 2
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
                tab.setIcon(tabSelectedIcons[position])
            }.attach()
        }

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showViewPager()
        }
    }

    override fun changeFragment(user: User) {
        hideViewPager()

        fragmentManager.beginTransaction()
            .add(R.id.frame_layout, ContactDetailFragment.newInstance(user))
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    private fun showViewPager() = with(binding) {
        frameLayout.isVisible = false
        pager.isVisible = true
        tabLayout.isVisible = true
    }

    fun hideViewPager() = with(binding) {
        frameLayout.isVisible = true
        pager.isVisible = false
        tabLayout.isVisible = false
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab!!.position) {
            // 0번째 탭 눌렀을 때
            0 -> {
                viewPager.setCurrentItem(0, false)
                tab.setIcon(tabSelectedIcons[tab.position]);
            }
            // 1번째 탭 눌렀을 때
            1 -> {
                viewPager.setCurrentItem(1, false)
                tab.setIcon(tabSelectedIcons[tab.position]);
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        when (tab!!.position) {
            // 0번째 탭 눌렀을 때
            0 -> {
                tab.setIcon(tabUnselectedIcons[tab.position]);
            }
            // 1번째 탭 눌렀을 때
            1 -> {
                tab.setIcon(tabUnselectedIcons[tab.position]);
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) { }
}