package com.android.santa_list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentAlertDialogBinding

private const val ARG_FRIEND = "FRIEND"

class AlertDialogFragment : DialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    val binding get() = _binding!!
    private var selectedAlarm = 0
    private var friend: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getParcelable(ARG_FRIEND, User::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //5초뒤
        _binding?.btnAlertDialog1?.setOnClickListener {
            selectedAlarm = 1
        }
        //하루 전
        _binding?.btnAlertDialog2?.setOnClickListener {
            selectedAlarm = 2
        }
        //당일
        _binding?.btnAlertDialog3?.setOnClickListener {
            selectedAlarm = 3
        }
        //완료
        _binding?.alertBtnDialogComplete?.setOnClickListener {
            exit()
        }
        //취소
        _binding?.alertBtnDialogBack?.setOnClickListener {
            selectedAlarm = 0
            exit()
        }
    }

    //디테일페이지로 돌아가는 함수
    private fun exit() {
        val dialogResult =
            ContactDetailFragment.newInstance(friend!!, selectedAlarm)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, dialogResult).addToBackStack(null).commit()
        dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            AlertDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FRIEND, user)
                }
            }
    }
}