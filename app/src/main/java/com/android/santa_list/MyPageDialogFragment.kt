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
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.android.santa_list.dataClass.Dummy.myData
import com.android.santa_list.databinding.FragmentMyPageDialogBinding
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageDialogFragment : DialogFragment() {
    private var _binding: FragmentMyPageDialogBinding? = null
    val binding get() = _binding!!
    val selectDate: Array<Int?> = arrayOfNulls<Int>(3)
    // Fragment에 데이터를 넘겨주기 위한 인터페이스
    interface FragmentInterfacer {
        fun onButtonClick(input: String)
    }

    private var fragmentInterfacer: FragmentInterfacer? = null
    fun setFragmentInterfacer(fragmentInterfacer: FragmentInterfacer?) {
        this.fragmentInterfacer = fragmentInterfacer
    }

    private var pickUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyPageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 이미지 불러오기

        with (binding) {
            if (myData[0].uri != "") {
                dialogIv.setImageURI(myData[0].uri!!.toUri())
            } else {
                dialogIv.setImageResource(R.drawable.image_add_image)
            }
            detailEtEmail.setText(myData[0].email ?: "") // 이메일을 설정해 주세요.
            detailEtTel.setText(myData[0].phone_number ?: "") // 전화번호를 설정해 주세요.
            detailEtName.setText(myData[0].name ?: "") // 이름을 설정해 주세요.
            if (myData[0].gift_date[0] != null) {
                detailGiftDate.text = "${myData[0].gift_date[0]}년 ${myData[0].gift_date[1]}월 ${myData[0].gift_date[2]}일"
            } else {
                detailGiftDate.hint = "나에게 선물 해줄 날짜"
            }

            // 이미지 선택
            dialogIv.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            // 전화번호 자동 하이픈 ( - )
            detailEtTel.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            // 달력 다이얼로그 출력
            detailGiftDate.setOnClickListener {
                dialogCalendal()
            }
        }
        // 다이얼로그 열었을 때 selectDate Text가 있는데 null로 나온다면
        if (selectDate[0] == null && binding.detailGiftDate.text.isNotBlank()) {
            val dateText: List<String> = binding.detailGiftDate.text.split("년 ", "월 ", "일")
            for (i in 0..2) {
                selectDate[i] = dateText[i].toInt()
            }
        }
        // 버튼 클릭시 이벤트
        val btnOk = binding.btnOk
        val emailPattern = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
        btnOk.setOnClickListener {
            when {
                myData[0].uri.isNullOrBlank() && pickUri == null -> Toast.makeText(this.requireContext(), "프로필 이미지를 확인해주세요.", Toast.LENGTH_SHORT).show()
                binding.detailEtName.text.isNullOrBlank() -> Toast.makeText(this.requireContext(), "이름을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                binding.detailEtTel.text.isNullOrBlank() -> Toast.makeText(this.requireContext(), "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                !Pattern.matches(emailPattern, binding.detailEtEmail.text) -> Toast.makeText(this.requireContext(), "이메일의 형식이 올바르지 않거나, 비어있습니다.", Toast.LENGTH_SHORT).show()
                // 날짜를 불러왔을때 날짜를 바로 입력이 가능하도록
                selectDate[0] == null && binding.detailGiftDate.text.isNullOrBlank() -> Toast.makeText(this.requireContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                else -> {
                    with(myData[0]) {
                        gift_date = selectDate
                        email = binding.detailEtEmail.text.toString()
                        phone_number = binding.detailEtTel.text.toString()
                        if (pickUri != null) {
                            uri = pickUri.toString()
                        }
                        name = binding.detailEtName.text.toString()
                    }
                    dismiss()
                }
            }
            // 마이페이지 프래그먼트로 돌아갈 때 데이터 다시 적용 실행시키기
            val resultBundle = bundleOf("dataSend" to "dataSend")
            setFragmentResult("dataSend", resultBundle)
        }
        val btnBack = binding.btnBack
        btnBack.setOnClickListener() {
            dismiss()
        }
    }

    // 달력 다이얼로그 출력
    private fun dialogCalendal () {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            // i년 i2월 i3일
            selectDate[0] = i
            selectDate[1] = i2 + 1
            selectDate[2] = i3
            binding.detailGiftDate.text = "${i}년 ${i2 + 1}월 ${i3}일"
        }
        val picker = DatePickerDialog(requireContext(), listener, year, month, day)
        picker.show()
    }
    // 이미지 선택
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // 사진 선택 이후 돌아왔을 때 콜백
        if (uri != null) {
            // 선택된 사진이 있을 경우
            pickUri = uri
            binding.dialogIv.setImageURI(uri)
        } else {
            // 선택된 사진이 없을 경우
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPageDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                MyPageDialogFragment().apply {
                    arguments = Bundle().apply { }
                }
    }
}