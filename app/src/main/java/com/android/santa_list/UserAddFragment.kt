package com.android.santa_list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.dataClass.UserGroup
import com.android.santa_list.databinding.FragmentUserAddBinding
import java.time.LocalDateTime
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [UserAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserAddFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var contactListFragment: ContactListFragment? = null

    private var _binding: FragmentUserAddBinding? = null
    private val binding get() = _binding!!
    private var pickURI: Uri? = null

    private var year = 0
    private var month = 0
    private var day = 0

    private var userGroup = UserGroup.NONE

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            pickURI = uri
            Log.d("whatHappen", "${uri::class.java}")
            binding.dialogIv.setImageURI(uri)
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("UserAddFragment", "onCreate = ${Dummy.dummy_users.size}")
        arguments?.let {
            contactListFragment = it.getParcelable(ARG_PARAM1, ContactListFragment::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val now = LocalDateTime.now()

        binding.dialogIv.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.d("whatHappen","현재 누르고있습니다")
        }

        binding.tvWantToPresentDate.setOnClickListener {
            dialogCalendal()
        }

        binding.tvAddGroupCategory.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("그룹 선택")

            val inflater = layoutInflater.inflate(R.layout.dialog_group_category, null)
            builder.setView(inflater)

            val listener = DialogInterface.OnClickListener { p0, p1 ->

                val alertDialog = p0 as AlertDialog
                val family = alertDialog.findViewById<RadioButton>(R.id.category_family)
                val friend = alertDialog.findViewById<RadioButton>(R.id.category_friend)
                val company = alertDialog.findViewById<RadioButton>(R.id.category_company)
                val school = alertDialog.findViewById<RadioButton>(R.id.category_school)

                userGroup = when {
                    family.isChecked -> UserGroup.FAMILY
                    friend.isChecked -> UserGroup.FRIEND
                    company.isChecked -> UserGroup.COMPANY
                    school.isChecked -> UserGroup.SCHOOL
                    else -> UserGroup.NONE
                }
                binding.tvAddGroupCategory.text = userGroup.groupName
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)
            builder.show()
        }

        binding.btnOk.setOnClickListener {
            val name = binding.etAddName.text.toString()
            val email = binding.etAddEmail.text.toString()
            val tel =  binding.etAddTel.text.toString()


            when{
                pickURI == null -> Toast.makeText(this.requireContext(), "프로필 이미지를 확인해주세요.", Toast.LENGTH_SHORT).show()
                name.isBlank() -> Toast.makeText(this.requireContext(), "이름을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                tel.isBlank() -> Toast.makeText(this.requireContext(), "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                !Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", email) -> Toast.makeText(this.requireContext(), "이메일의 형식이 올바르지 않거나, 비어있습니다.", Toast.LENGTH_SHORT).show()
                year == 0 -> Toast.makeText(this.requireContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                else -> {
                    val user = User(
                        "id",
                        name,
                        tel,
                        email,
                        userGroup,
                        profile_image_uri = pickURI.toString()
                    )
                    Dummy.dummy_users.add(user)
                    requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                }
            }

        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ContactListFragment) =
            UserAddFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }


    private fun dialogCalendal () {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

            this.year = year
            this.month = month+1
            this.day = day

            binding.tvWantToPresentDate.text = "${year}년 ${month + 1}월 ${day}일"
        }
        val picker = DatePickerDialog(requireContext(), listener, year, month, day)
        picker.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroy() {
        super.onDestroy()
//        contactListFragment?.mainAdapter?.notifyDataSetChanged()
        contactListFragment?.notifyDataSetChanged()
    }

}