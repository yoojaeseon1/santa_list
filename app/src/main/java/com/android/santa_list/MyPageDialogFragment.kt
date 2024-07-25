package com.android.santa_list

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.android.santa_list.dataClass.Dummy.MyData
import com.android.santa_list.dataClass.Dummy.myData
import com.android.santa_list.databinding.FragmentMyPageDialogBinding
import java.net.URI

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageDialogFragment : DialogFragment() {
    private var _binding: FragmentMyPageDialogBinding? = null
    val binding get() = _binding!!
    // Fragment에 데이터를 넘겨주기 위한 인터페이스
    interface FragmentInterfacer {
        fun onButtonClick(input: String)
    }

    private var fragmentInterfacer: FragmentInterfacer? = null
    fun setFragmentInterfacer(fragmentInterfacer: FragmentInterfacer?) {
        this.fragmentInterfacer = fragmentInterfacer
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var pickURI: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyPageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 이미지 선택
        binding.dialogIv.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            Log.d("whatHappen","현재 누르고있습니다")
        }
        // 달력 다이얼로그 출력
        binding.detailGiftDate.setOnClickListener {
            dialogCalendal()
        }
        // 버튼 클릭시 이벤트
        val btnOk = binding.btnOk
        btnOk.setOnClickListener {
            myData[0].uri = pickURI?.toString()
            myData[0].name = binding.detailEtName.text.toString()
            myData[0].email = binding.detailEtEmail.text.toString()
            myData[0].phone_number = binding.detailEtTel.text.toString()
            myData[0].gift_date = binding.detailGiftDate.text.toString()
            val resultBundle = bundleOf("dataSend" to "dataSend")
            setFragmentResult("dataSend", resultBundle)
//            기존 번틀을 통해서 데이터를 전송하는 방식 -> dummy의 MyData 에 저장으로 바뀌는 걸로 변경됨
//            val bundle = bundleOf(
//                "uriStr" to pickURI?.toString(),
//                "name" to binding.detailEtName.text.toString(),
//                "email" to binding.detailEtEmail.text.toString(),
//                "tel" to binding.detailEtTel.text.toString(),
//                "giftDate" to binding.detailGiftDate.text
//            )
//            setFragmentResult("requestKey", bundle)
            dismiss()
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
            pickURI = uri
            Log.d("whatHappen", "${uri::class.java}")
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
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}