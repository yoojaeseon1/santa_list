package com.android.santa_list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.android.santa_list.databinding.ActivityContactBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ContactActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private val binding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(
            layoutInflater
        )
    }
    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var viewPager: ViewPager2
    private val tabTitles = arrayOf("연락처", "내정보")

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
            tabLayout.tabLayout.addOnTabSelectedListener(this@ContactActivity) // 다형성을 추가한 것

            viewPager = pager
            viewPager.adapter = ViewPager2Adapter(this@ContactActivity)
            TabLayoutMediator(tabLayout.tabLayout, viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }

        val contactListFragment = ContactListFragment()
        setFragment(contactListFragment)
    }

    private fun setFragment(fragment: Fragment) {
        fragmentManager.commit {
//            replace(R.id.frame_layout, fragment) << ViewPager2 동작할 때 UI 이상하게 출력되는 원인이므로 주석 처리
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab!!.position) {
            // 0번째 탭 눌렀을 때
            0 -> {
                setFragment(ContactListFragment())
            }
            // 1번째 탭 눌렀을 때
            1 -> {
                setFragment(MyPageFragment())
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}