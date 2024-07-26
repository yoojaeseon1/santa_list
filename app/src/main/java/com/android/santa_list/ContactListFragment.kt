package com.android.santa_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.dataClass.UserGroup
import com.android.santa_list.databinding.FragmentContactListBinding
import com.android.santa_list.repository.PresentLogRepository


// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// 단일 책임의 원칙과, 최소 놀람의 법칙 (내 코드를 모르는 개발자가 봐도 덜 놀라야 됨...)
// AAC? 안드로이드 아키텍처!의 뷰모델은 LifeCycle가 돌아가는 동안 data를 유지함 (Data Holding 역할)

class ContactListFragment : Fragment(), MainRecyclerViewAdapter.OnStarredChangeListener {
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentContactListBinding by lazy {
        FragmentContactListBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var presentLogRepository: PresentLogRepository

    private val contactList: MutableList<User> = Dummy.dummyUserList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

//         Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isStarredList()

        recyclerView = binding.contactRecyclerView
        presentLogRepository = PresentLogRepository()

        adapter = MainRecyclerViewAdapter(context, contactList, recyclerView, object: MainRecyclerViewAdapter.OnStarredChangeListener{
            override fun onStarredChanged() {
                isStarredList()
            }
        })

        adapter.itemClick = object : MainRecyclerViewAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val user = contactList[position]
//                user.is_starred = !user.is_starred
//                Log.d("즐겨찾기", "${user.is_starred}")
                isStarredList()
                adapter.notifyItemChanged(position)

            }
        }

        binding.toolBar.action.setOnClickListener {
            val popup = PopupMenu(context, it)
            onClickMore(popup)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.catetory_array,
            android.R.layout.simple_spinner_item
        ). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.listFilteringSpinner.adapter = adapter
        }

        binding.listFilteringSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var filteredList = mutableListOf<User>()
                when(position) {
                    0 -> {
                        filteredList = contactList
                    }
                    1 -> { // 선물 해줄 사람(나한테 준 사람)
                        filteredList = presentLogRepository.selectGiveUserList()
                    }
                    2 -> { // (내가) 선물 해준 사람
                        filteredList = presentLogRepository.selectReceivedUserList()
                    }
                    3 -> {
                        filteredList =
                            contactList.filter { it.group == UserGroup.FAMILY }.toMutableList()
                    }
                    4 -> {
                        filteredList =
                            contactList.filter { it.group == UserGroup.FRIEND }.toMutableList()
                    }
                    5 -> {
                        filteredList =
                            contactList.filter { it.group == UserGroup.COMPANY }.toMutableList()
                    }
                    6 -> {
                        filteredList =
                            contactList.filter { it.group == UserGroup.SCHOOL }.toMutableList()
                    }
                }

                adapter = MainRecyclerViewAdapter(context, filteredList, recyclerView, object: MainRecyclerViewAdapter.OnStarredChangeListener{
                    override fun onStarredChanged() {
                        isStarredList()
                    }
                })
                recyclerView.adapter = adapter

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

    }

    override fun onStarredChanged() {
        isStarredList()
    }

    private fun isStarredList() {
        val recyclerView = binding.contactIsStarredRecyclerView
        val isStarredList : MutableList<User> = Dummy.dummyUserList().filter { it.is_starred }.toMutableList()
        val adapter = ContactIsStarredAdapter(isStarredList)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun onClickMore(popup: PopupMenu) {
        val linearLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context)
        val gridLayoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 4)

        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main_menu_option, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_list -> {
                    Log.d("🤔 ??", "리스트 뷰 선택")
                    recyclerView.layoutManager = linearLayoutManager
                }

                R.id.menu_item_grid -> {
                    Log.d("🤔 ??", "그리드 뷰 선택")
                    recyclerView.layoutManager = gridLayoutManager
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