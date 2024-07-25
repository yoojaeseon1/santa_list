package com.android.santa_list

import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.fragment.app.findFragment

import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.databinding.ActivityContactBinding
import com.google.android.material.tabs.TabLayout

class ContactActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private val binding: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(
            layoutInflater
        )
    }
    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // binding 관련된 코드들 추가할 때 여기서
        binding.run {
            tabLayout.tabLayout.addOnTabSelectedListener(this@ContactActivity) // 다형성을 추가한 것


            // 툴바의 action icon 클릭할 때 동작 하는 부분 >>
            // TODO: ❗️❗️❗️ 각 프래그먼트로 작성한 함수 이동 해야 합니다 ❗️❗️❗️
//            toolBar.action.setOnClickListener {
//                val currentFragment: Fragment? = fragmentManager.findFragmentById(R.id.frame_layout)
//                if (currentFragment != null) {
//                    when (currentFragment) {
//                        is ContactListFragment -> {
//                            val popup = PopupMenu(this@ContactActivity, it)
//
//                            onClickMore(popup, currentFragment)
//                        }
//
//                        is MyPageFragment -> {
//                            Log.d("⏰ action Click", "마이 페이지입니다.")
//                        }
//
//                        else -> {
//                            Log.d("⏰ action Click", "다른 화면입니다.")
//                        }
//                    }
//                }
//            }

            // 툴바의 action icon 클릭할 때 동작 하는 부분
//             toolBar.action.setOnClickListener {
//                 val currentFragment: Fragment? = fragmentManager.findFragmentById(R.id.frame_layout)
//                 if (currentFragment != null) {
//                     when (currentFragment) {
//                         is ContactListFragment -> {
//                             val popup: PopupMenu = PopupMenu(this@ContactActivity, it)
//                             onClickMore(popup)
//                         }

//                         is MyPageFragment -> {
//                             val myPageDialog = MyPageDialogFragment()
//                             myPageDialog.show(fragmentManager, "DialogFragment")
//                         }

//                         else -> {
//                             Log.d("⏰ action Click", "다른 화면입니다.")
//                         }
//                     }
//                 }
//             }

        }

        val contactDetailFragment = ContactDetailFragment.newInstance(Dummy.hwamin)
        // 임시 개발용
        setFragment(contactDetailFragment)
    }

    private fun setFragment(fragment: Fragment) {
        fragmentManager.commit {
            replace(R.id.frame_layout, fragment)
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
//                binding.toolBar.action.setImageResource(R.drawable.ic_edit)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}