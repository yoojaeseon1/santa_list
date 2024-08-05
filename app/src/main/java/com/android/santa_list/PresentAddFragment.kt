package com.android.santa_list

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.Present
import com.android.santa_list.dataClass.PresentLog
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentPresentAddBinding
import com.android.santa_list.repository.PresentLogRepository
import java.io.File
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [PresentAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PresentAddFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: User? = null
    private var param2: String? = null
    private var param3: ContactDetailFragment? = null
    private var _binding: FragmentPresentAddBinding? = null
    private val binding get() = _binding!!
    private val santaUtil = SantaUtil.getInstance()

    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val loginedUser = Dummy.loggedInUser

    private val presentLogRepository = PresentLogRepository()


    private var imageFile = File("")
    private var imageUri: Uri? = null
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            imageUri = result.data?.data
            imageUri?.let {
//                imageFile = File(santaUtil.getRealPathFromURI(requireActivity(), imageUri!!))
                imageFile = File(santaUtil.getRealPathFromURI(requireActivity(), imageUri!!))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1, User::class.java)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getParcelable(ARG_PARAM3, ContactDetailFragment::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPresentAddBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(param2 == "wish") {
            binding.tvPresentDate.visibility = ViewGroup.INVISIBLE
            binding.tvPresentDate.text = "${year}년 ${month + 1}월 ${day}일"
        }

        binding.ivAddPresent.setOnClickListener {

            if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)  {
                requireActivity().requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }

            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"
            )
            imageLauncher.launch(intent)
        }



//        binding.ivAddPresent.setOnClickListener()

        binding.tvPresentDate.setOnClickListener {
            val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                this.year = year
                this.month = month + 1
                this.day = dayOfMonth
                binding.tvPresentDate.text = "${year}년 ${month + 1}월 ${day}일"
            }
            val picker = DatePickerDialog(requireContext(), listener, year, month, day)
            picker.show()
        }




        binding.btnPresentComplete.setOnClickListener {

            // validation

            val present_name = binding.etPresentName.text.toString()
            val present_date = binding.tvPresentDate.text.toString()

            if(present_name.isEmpty() && present_date.isEmpty() && imageUri == null)
                Toast.makeText(requireActivity(), "선물 사진, 선물 명, 날짜를 입력 해주세요.", Toast.LENGTH_SHORT).show()
            else if(present_name.isEmpty())
                Toast.makeText(requireActivity(), "선물 명을 입력 해주세요.", Toast.LENGTH_SHORT).show()
            else if(present_date.isEmpty())
                Toast.makeText(requireActivity(), "선물 날짜를 입력 해주세요.", Toast.LENGTH_SHORT).show()
            else if(imageUri == null) {
                Toast.makeText(requireActivity(), "사진을 추가 해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // save data
                when(param2) {
                    "received" -> {
                        Dummy.presentLogs.add(PresentLog(
                            param1!!,
                            loginedUser,
                            Present(present_name, imageUri.toString()), LocalDateTime.of(year,month,day,0,0,0)))
                    }
                    "give" ->{
                        Dummy.presentLogs.add(PresentLog(
                            loginedUser,
                            param1!!,
                            Present(present_name, imageUri.toString()), LocalDateTime.of(year,month,day,0,0,0)))
                    }
                    else -> {
                        param1!!.wish_list.add(Present(present_name, imageUri.toString()))
                    }
                }
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            }
        }
        
        binding.btnPresentCancel.setOnClickListener {
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
         * @return A new instance of fragment PresentAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: User, from: String, fragment: ContactDetailFragment) =
            PresentAddFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, from)
                    putParcelable(ARG_PARAM3, fragment)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        Log.d("PresentAddFragment","onResume()")
        if(imageFile != null) {
            Log.d("PresentAddFragment","${imageUri}")
            binding.ivAddPresent.setImageURI(imageUri)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PresentAddFragment","onDestroy()")
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDetach() {
        super.onDetach()
        val receivedPresents = presentLogRepository.selectPresentList(param1!!, Dummy.loggedInUser)
        param3?.receivedPresentAdapter?.submitList(receivedPresents.makePresentList())

        val givePresents = presentLogRepository.selectPresentList(Dummy.loggedInUser, param1!!)
        param3?.givePresentAdapter?.submitList(givePresents.makePresentList())

        val wishList = param1!!.wish_list
        param3?.wishPresentAdapter?.submitList(wishList.makePresentList())

    }

}

