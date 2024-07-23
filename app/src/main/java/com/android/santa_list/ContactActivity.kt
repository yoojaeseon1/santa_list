package com.android.santa_list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.databinding.ActivityContactBinding
import com.google.android.material.tabs.TabLayout

class ContactActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private val binding: ActivityContactBinding by lazy { ActivityContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.run{
            tabLayout.tabLayout.addOnTabSelectedListener(this@ContactActivity) // 다형성을 추가한 것
        }
    }

    private fun setFragment(fragment: ContactDetailFragment) {
        supportFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
<<<<<<< Updated upstream
=======

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab!!.position){
            // 0번째 탭 눌렀을 때
            0 -> {
                setFragment(ContactListFragment())
                binding.toolBar.action.setImageResource(R.drawable.ic_more)
            }
            // 1번째 탭 눌렀을 때
            1-> {
                setFragment(MyPageFragment())
                binding.toolBar.action.setImageResource(R.drawable.ic_edit)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }
>>>>>>> Stashed changes
}