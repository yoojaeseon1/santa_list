package com.android.santa_list

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentAddContactDialogBinding
import com.android.santa_list.databinding.FragmentAlertDialogBinding
import com.android.santa_list.databinding.FragmentGiftShopDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GiftShopDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlertDialogFragment : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    val binding get() = _binding!!


    private var param1: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1, User::class.java)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("dd" , "${param1}")

        var selectedAlarm = 100




        _binding?.btnAlertDialog1?.setOnClickListener {
            selectedAlarm = 1
            Log.d("TAG", "${selectedAlarm}")
        }

        _binding?.btnAlertDialog2?.setOnClickListener {
            selectedAlarm = 2
            Log.d("TAG", "${selectedAlarm}")
        }

        _binding?.btnAlertDialog3?.setOnClickListener {
            selectedAlarm = 3
            Log.d("TAG", "${selectedAlarm}")
        }

        _binding?.alertBtnDialogComplete?.setOnClickListener {
            val dialogResult =
                ContactDetailFragment.newInstance(param1!!, selectedAlarm)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, dialogResult).addToBackStack(null).commit()
            Log.d("보냅니다", "${selectedAlarm}")
            val resultBundle = bundleOf("dataSend" to "dataSend")
            setFragmentResult("dataSend", resultBundle)
            dismiss()
        }
        _binding?.alertBtnDialogBack?.setOnClickListener {
            selectedAlarm = 777
            dismiss()

            val resultBundle = bundleOf("dataSend" to "dataSend")
            setFragmentResult("dataSend", resultBundle)

        }


//        val btnBackListener = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                selectedAlarm = 0
//                dismiss()
//            }
//        }
//        onBackPressedDispatcher.addCallback(this, btnBackListener)

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GiftShopDialogFragment.
         */
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GiftShopDialogFragment.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user : User) =
            AlertDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }
}