package com.android.santa_list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import com.android.santa_list.dataClass.Dummy.dummy_users
//import com.android.santa_list.dataClass.Dummy.dummyUserList
import com.android.santa_list.databinding.FragmentMyPageBinding
import com.android.santa_list.dataClass.Dummy.myData
import java.util.Random


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyPageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyPageBinding
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
        binding = FragmentMyPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("dataSend") { key, bundle ->
            setMyPageData()
        }
        setMyPageData()
        binding.toolBar.action.setOnClickListener {
            val myPageDialog = MyPageDialogFragment()
            myPageDialog.show(requireFragmentManager(), "DialogFragment")
        }

        val random = Random()
        val inputNumber = dummy_users[random.nextInt(dummy_users.size)].phone_number
        binding.mypageBtnCall.setOnClickListener {
            val intentCall = Intent(Intent.ACTION_DIAL)
            intentCall.data = Uri.parse("tel:$inputNumber")
            startActivity(intentCall)
        }
        binding.mypageBtnMessage.setOnClickListener {
            val intentMessage = Intent(Intent.ACTION_SENDTO)
            intentMessage.data = Uri.parse("smsto:$inputNumber")
            startActivity(intentMessage)
        }
    }

    override fun onResume() {
        super.onResume()
        setMyPageData()
    }

    private fun setMyPageData() {
        if (myData[0].santa_number.isNullOrBlank()) {
            myData[0].santa_number = randomSantaNumber.invoke()
//            myData[0].santa_number = randomSantaNumber()
        }

        binding.mypageTvSetSantaNumber.text = myData[0].santa_number
        binding.mypageIvProfile.setImageURI(myData[0].uri?.toUri())
        binding.mypageTvName.text = myData[0].name
        binding.mypageTvSetPhoneNumber.text = myData[0].phone_number
        binding.mypageTvSetEmail.text = myData[0].email
        if (myData[0].gift_date[0] == null) {
            binding.mypageTvSetGiftDate.text = ""
        } else {
            binding.mypageTvSetGiftDate.text =
                "${myData[0].gift_date[0]}년 ${myData[0].gift_date[1]}월 ${myData[0].gift_date[2]}일"
        }
    }

//    private fun randomSantaNumber(): String {
//        var santaNumber: String = ""
//        val random = Random()
//        repeat(6) {
//            santaNumber += random.nextInt(10).toString()
//        }
//        santaNumber += "-"
//        repeat(7) {
//            santaNumber += random.nextInt(10).toString()
//        }
//        return santaNumber
//    }

    private val randomSantaNumber = {
        var santaNumber = ""
        val random = Random()
        repeat(6) {
            santaNumber += random.nextInt(10).toString()
        }
        santaNumber += "-"
        repeat(7) {
            santaNumber += random.nextInt(10).toString()
        }

        santaNumber
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}