package com.android.santa_list

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.android.santa_list.dataClass.Dummy.myData
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentAddContactDialogBinding
import java.time.LocalDateTime
import java.util.regex.Pattern

private const val ARG_FRIEND = "FRIEND"

class AddContactDialogFragment : DialogFragment() {
    private var _binding: FragmentAddContactDialogBinding? = null
    val binding get() = _binding!!
    private var pickURI: Uri? = null
    private val selectDate = arrayOf(0, 0, 0)
    private var friend = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getParcelable(ARG_FRIEND, User::class.java) ?: User()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //기존 데이터 연결
        getProfile()

        // 이미지 추가 클릭 : 사용자 카메라 또는 갤러리에서 이미지 업로드
        binding.detailIvAddDialg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        // 선물 해줄 날짜 클릭 : 달력 다이얼로그 호출
        binding.detailBtnDialogPresentDay.setOnClickListener {
            dialogCalendar()
        }
        // 완료 버튼 : 유효성검사에 따른 토스트메시지
        binding.alertBtnDialogComplete.setOnClickListener {
            when {
                binding.detailEtAddDialgName.text.isNullOrBlank() -> Toast.makeText(
                    this.requireContext(),
                    getString(R.string.please_check_name),
                    Toast.LENGTH_SHORT
                ).show()

                binding.detailEtAddDialgPhoneNumber.text.isNullOrBlank() -> Toast.makeText(
                    this.requireContext(),
                    getString(R.string.please_check_phone_number),
                    Toast.LENGTH_SHORT
                ).show()

                !Pattern.matches(
                    "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
                    binding.detailEtAddDialgEmail.text
                ) -> Toast.makeText(
                    this.requireContext(),
                    "이메일의 형식이 올바르지 않거나, 비어있습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                selectDate[0] == 0 -> Toast.makeText(
                    this.requireContext(),
                    getString(R.string.please_check_date),
                    Toast.LENGTH_SHORT
                ).show()

                else -> {
//                    if (friend?.profile_image_uri != "") { friend?.profile_image_uri = pickURI.toString() }
                    if(pickURI != null) {
                        friend.profile_image_uri = pickURI.toString()
                        friend.profile_image = -1
                    }
                    friend.name = binding.detailEtAddDialgName.text.toString()
                    friend.phone_number = binding.detailEtAddDialgPhoneNumber.text.toString()
                    friend.email = binding.detailEtAddDialgEmail.text.toString()
                    friend.event_date =
                        LocalDateTime.of(selectDate[0], selectDate[1], selectDate[2], 0, 0, 0)

                    val dialogResult =
                        ContactDetailFragment.newInstance(friend!!)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, dialogResult).addToBackStack(null).commit()
                    dismiss()
                }
            }
        }
        val btnBack = binding.alertBtnDialogBack
        btnBack.setOnClickListener() {
            dismiss()
        }
    }

    fun getProfile() {
//        if (myData[0].uri != "") {
//            binding.detailIvAddDialg.setImageURI(friend?.profile_image_uri!!.toUri())
//        } else if(){
//            binding.detailIvAddDialg.setImageResource(R.drawable.image_add_image)
//        }
        if (friend.profile_image_uri != "") {
            binding.detailIvAddDialg.setImageURI(friend?.profile_image_uri!!.toUri())
        } else if(friend.profile_image >= 0){
            binding.detailIvAddDialg.setImageResource(friend.profile_image)
        } else
            binding.detailIvAddDialg.setImageResource(R.drawable.image_add_image)
        with(binding) {
            detailEtAddDialgEmail.setText(friend?.email ?: "")
            detailEtAddDialgPhoneNumber.setText(friend?.phone_number ?: "")
            detailEtAddDialgName.setText(friend?.name ?: "")
            detailEtAddDialgPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }
    }

    // 달력 다이얼로그 출력
    private fun dialogCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            selectDate[0] = i
            selectDate[1] = i2 + 1
            selectDate[2] = i3
            binding.detailBtnDialogPresentDay.text = "${i}년 ${i2 + 1}월 ${i3}일"
        }
        val picker = DatePickerDialog(requireContext(), listener, year, month, day)
        picker.show()
    }

    // 이미지 선택
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                pickURI = uri
                Log.d("whatHappen", "${uri::class.java}")
                binding.detailIvAddDialg.setImageURI(uri)
            }
        }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            AddContactDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FRIEND, user)
                }
            }
    }
}