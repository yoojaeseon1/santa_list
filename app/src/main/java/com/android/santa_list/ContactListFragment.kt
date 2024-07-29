package com.android.santa_list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.dataClass.UserGroup
import com.android.santa_list.databinding.FragmentContactListBinding
import com.android.santa_list.repository.PresentLogRepository
import kotlinx.parcelize.Parcelize

interface ChangeFragmentListener {
    fun changeFragment(user: User)
}

// 단일 책임의 원칙과, 최소 놀람의 법칙 (내 코드를 모르는 개발자가 봐도 덜 놀라야 됨...)
// AAC? 안드로이드 아키텍처!의 뷰모델은 LifeCycle가 돌아가는 동안 data를 유지함 (Data Holding 역할)
@Parcelize
class ContactListFragment : Fragment(), MainRecyclerViewAdapter.OnStarredChangeListener, Parcelable {
    private val binding: FragmentContactListBinding by lazy {
        FragmentContactListBinding.inflate(
            layoutInflater
        )
    }
    private var changeFragmentListener: ChangeFragmentListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainAdapter: MainRecyclerViewAdapter
    private lateinit var presentLogRepository: PresentLogRepository

    private val contactList: MutableList<User> = Dummy.dummy_users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    // 프래그먼트가 액티비티에 붙어있을 때 호출되는 메서드
    // 프래그먼트가 액티비티의 UI에 추가되기 직전에 실행
    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeFragmentListener = context as ChangeFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isStarredList()
        Log.d("ContactListFragment", "onViewCreated")

        recyclerView = binding.contactRecyclerView
//        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))

        presentLogRepository = PresentLogRepository()

        mainAdapter = MainRecyclerViewAdapter(
            { user ->
                changeFragmentListener?.changeFragment(user)
            },
            context,
            contactList,
            recyclerView,
            object : MainRecyclerViewAdapter.OnStarredChangeListener {
                override fun onStarredChanged() {
                    isStarredList()
                }
            })


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.catetory_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.listFilteringSpinner.adapter = adapter
        }

        with(mainAdapter) {
            itemClick = object : MainRecyclerViewAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    isStarredList()
                    mainAdapter.notifyItemChanged(position)
                }
            }
        }

        with(binding) {
            toolBar.action.setOnClickListener {
                val popup = PopupMenu(context, it)
                onClickMore(popup, recyclerView)
            }

            listFilteringSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        var filteredList = mutableListOf<User>()
                        when (position) {
                            0 -> {
                                Log.d("contactListFragment", "start filter 0")
                                filteredList = contactList
                            }

                            1 -> { // 선물 해줄 사람(나한테 준 사람)
                                filteredList = presentLogRepository.selectGiveUserList()
//                        presentLogRepository.selectGiveUserList(contactList)
                            }

                            2 -> { // (내가) 선물 해준 사람
                                filteredList = presentLogRepository.selectReceivedUserList()
                            }

                            3 -> {
                                filteredList =
                                    contactList.filter { it.group == UserGroup.FAMILY }
                                        .toMutableList()
                            }

                            4 -> {
                                filteredList =
                                    contactList.filter { it.group == UserGroup.FRIEND }
                                        .toMutableList()
                            }

                            5 -> {
                                filteredList =
                                    contactList.filter { it.group == UserGroup.COMPANY }
                                        .toMutableList()
                            }

                            6 -> {
                                filteredList =
                                    contactList.filter { it.group == UserGroup.SCHOOL }
                                        .toMutableList()
                            }
                        }
                        filteredList.sortWith{ o1, o2 -> o1.name.compareTo(o2.name) }
                        mainAdapter.contact = filteredList
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = mainAdapter
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        return
                    }
                }

            btnAddUser.setOnClickListener {
                val userAddFragment = UserAddFragment.newInstance(this@ContactListFragment)
                userAddFragment.show(requireActivity().supportFragmentManager, "dialogFragment")
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mainAdapter
    }

    override fun onStarredChanged() {
        isStarredList()
    }

    override fun onResume() {
        super.onResume()
        Log.d("ContactListFragment", "onResume")
        mainAdapter.notifyDataSetChanged()
    }

    fun isStarredList() {
        val recyclerView = binding.contactIsStarredRecyclerView
        val isStarredList: MutableList<User> =
            Dummy.dummy_users.filter { it.is_starred }.toMutableList()
        val adapter = ContactIsStarredAdapter(isStarredList)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    // Challenge 과제 1
    private fun onClickMore(popup: PopupMenu, recyclerView: RecyclerView) {
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main_menu_option, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_list -> {
                    Log.d("🤔 ??", "리스트 뷰 선택")
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }

                R.id.menu_item_grid -> {
                    Log.d("🤔 ??", "그리드 뷰 선택")
                    recyclerView.layoutManager = GridLayoutManager(context, 4)
                }

                else -> {
                    Log.d("🤔 ??", "뭐여?")
                }
            }
            false
        }

        popup.show()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onPause() {
        super.onPause()
        Log.d("ContactListFragment", "onPause")
    }

    fun initRecyclerView(user_list: MutableList<User>) {
        mainAdapter.contact = user_list
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mainAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataSetChanged() {
        mainAdapter.contact.sortWith{ o1, o2 -> o1.name.compareTo(o2.name) }
        mainAdapter.notifyDataSetChanged()
    }

}