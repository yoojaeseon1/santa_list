package com.android.santa_list

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.android.santa_list.MyPageDialogFragment.FragmentInterfacer
import com.android.santa_list.dataClass.Dummy.myData
import com.android.santa_list.databinding.FragmentAddContactDialogBinding
import com.android.santa_list.databinding.FragmentMyPageDialogBinding
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactDialogFragment : DialogFragment() {
    private var _binding: FragmentAddContactDialogBinding? = null
    val binding get() = _binding!!
    private var pickURI: Uri? = null
    val selectDate: Array<Int?> = arrayOfNulls<Int>(3)

    interface FragmentInterfacer {
        fun onButtonClick(input: String)
    }
    private var fragmentInterfacer: com.android.santa_list.AddContactDialogFragment.FragmentInterfacer? = null
    fun setFragmentInterfacer(fragmentInterfacer: com.android.santa_list.AddContactDialogFragment.FragmentInterfacer?) {
        this.fragmentInterfacer = fragmentInterfacer
    }

    private var param1: String? = null
    private var param2: String? = null


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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 이미지 불러오기

        if (myData[0].uri != "") {
            binding.detailIvAddDialg.setImageURI(myData[0].uri!!.toUri())
        } else {
            binding.detailIvAddDialg.setImageResource(R.drawable.image_add_image)
        }
        binding.detailEtAddDialgEmail.setText(myData[0].email ?: "") // 이메일을 설정해 주세요.
        binding.detailEtAddDialgPhoneNumber.setText(myData[0].phone_number ?: "") // 전화번호를 설정해 주세요.
        binding.detailEtAddDialgName.setText(myData[0].name ?: "") // 이름을 설정해 주세요.
        if (myData[0].gift_date[0] != null) {
            binding.detailBtnDialogPresentDay.text = "${myData[0].gift_date[0]}년 ${myData[0].gift_date[1]}월 ${myData[0].gift_date[2]}일"
        } else {
            binding.detailBtnDialogPresentDay.hint = "날짜를 설정해 주세요."
        }

        // 이미지 선택
        binding.detailIvAddDialg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.d("whatHappen","현재 누르고있습니다")
        }
        // 전화번호 자동 하이픈 ( - )
        binding.detailEtAddDialgPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        // 달력 다이얼로그 출력
        binding.detailBtnDialogPresentDay.setOnClickListener {
            dialogCalendal()
        }
        // 버튼 클릭시 이벤트
        val btnOk = binding.detailBtnDialogOk
        btnOk.setOnClickListener {

            when {
                pickURI == null -> Toast.makeText(this.requireContext(), "프로필 이미지를 확인해주세요.", Toast.LENGTH_SHORT).show()
                binding.detailEtAddDialgName.text.isNullOrBlank() -> Toast.makeText(this.requireContext(), "이름을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                binding.detailEtAddDialgPhoneNumber.text.isNullOrBlank() -> Toast.makeText(this.requireContext(), "전화번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                !Pattern.matches("[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", binding.detailEtAddDialgEmail.text) -> Toast.makeText(this.requireContext(), "이메일의 형식이 올바르지 않거나, 비어있습니다.", Toast.LENGTH_SHORT).show()
                selectDate[0] == null -> Toast.makeText(this.requireContext(), "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show()
                else -> {
                    myData[0].gift_date = selectDate
                    myData[0].email = binding.detailEtAddDialgEmail.text.toString()
                    myData[0].phone_number = binding.detailEtAddDialgPhoneNumber.text.toString()
                    myData[0].uri = pickURI?.toString()
                    myData[0].name = binding.detailEtAddDialgName.text.toString()
                    dismiss()
                }
            }
            // 마이페이지 프래그먼트로 돌아갈 때 데이터 다시 적용 실행시키기
            val resultBundle = bundleOf("dataSend" to "dataSend")
            setFragmentResult("dataSend", resultBundle)
        }
        val btnBack = binding.detailBtnDialogBack
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
            binding.detailBtnDialogPresentDay.text = "${i}년 ${i2 + 1}월 ${i3}일"
        }
        val picker = DatePickerDialog(requireContext(), listener, year, month, day)
        picker.show()
    }
    // 이미지 선택
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // 사진 선택 이후 돌아왔을 때 콜백
        if (uri != null) {
            // 선택된 사진이 있을 경우
            pickURI = uri
            Log.d("whatHappen", "${uri::class.java}")
            binding.detailIvAddDialg.setImageURI(uri)
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
         * @return A new instance of fragment AddContactDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddContactDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}