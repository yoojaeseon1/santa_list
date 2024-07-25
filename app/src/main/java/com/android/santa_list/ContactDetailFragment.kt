package com.android.santa_list


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentContactDetailBinding
import com.android.santa_list.repository.PresentLogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.parcelize.Parcelize


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
const val TAG = "ContactDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@Parcelize
class ContactDetailFragment : Fragment(), Parcelable {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val presentLogRepository = PresentLogRepository()
    private val santaUtil = SantaUtil.getInstance()

    val receivedPresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    val givePresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    val wishPresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }


    private var friend: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getParcelable(ARG_PARAM1, User::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//알림버튼 : 클릭 시 다이얼로그 응답에 따라 해당 시간에 알림, 알림 클릭시 디테일 페이지로 돌아옴

        alertListener(_binding?.detailIvAlert as View)
        alertListener(_binding?.detailTvAlert as View)


        //선물하기버튼 : 클릭 시 다이얼로그 응답에 따라 카카오톡, 쿠팡으로 이동
        _binding?.detailIvGift?.setOnClickListener {
            giftListener()
        }

        binding.detailTvName.text = friend?.name
        binding.detailTvSetPhoneNumber.text = friend?.phone_number
        binding.detailTvSetEmail.text = friend?.email
        binding.detailTvSetPresentDate.text = santaUtil.makeDateFormat(friend!!.event_date)

        val receivedPresents = presentLogRepository.selectPresentList(Dummy.loggedInUser, friend!!)
        receivedPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {

                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "received",this@ContactDetailFragment)

                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog"
                )
            }
        }
        receivedPresentAdapter.submitList(santaUtil.makePresentList(receivedPresents))


        val givePresents = presentLogRepository.selectPresentList(friend!!, Dummy.loggedInUser)
        givePresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "give", this@ContactDetailFragment)
                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog")
            }
        }
        givePresentAdapter.submitList(santaUtil.makePresentList(givePresents))

        val wishList = friend!!.wish_list
        wishPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "wish", this@ContactDetailFragment)
                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog")
            }
        }
        wishPresentAdapter.submitList(santaUtil.makePresentList(wishList))

        binding.detailRecyclerViewSonjulGo.adapter = receivedPresentAdapter
        binding.detailRecyclerViewPresentHistory.adapter = givePresentAdapter
        binding.detailRecyclerWishPresent.adapter = wishPresentAdapter



        binding.detailBtnMessage.setOnClickListener {
            val smsUri = Uri.parse("smsto:" + friend?.phone_number)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData(smsUri)
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }

        binding.detailBtnCall.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.d("contactDetailFragment", "don't have permission")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1
                )
            }
            else {
                Log.d("contactDetailFragment", "have permission")
                val phone_number = "tel:" + santaUtil.removePhoneHyphen(friend!!.phone_number)
                val intent = Intent("android.intent.action.CALL", Uri.parse(phone_number))
                startActivity(intent)
            }
        }

    }

    fun alertListener(view: View) {
        view.setOnClickListener {

//            initNotification()
            dialogAlarm()
        }
    }


    //알람리시버 설정 함수 : 사용자가 선택한 시간에 알림
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarmReceiver() {
        Toast.makeText(context, "알람설정완료", Toast.LENGTH_SHORT).show()

        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, 6) //0부터 시작한다
            set(Calendar.DAY_OF_MONTH, 25)
            set(Calendar.HOUR_OF_DAY, 9) //24시간으로 지정한다
            set(Calendar.MINUTE, 15)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, pendingIntent
        )
    }

//알림_다이얼로그 함수 : 사용자에게 알림시간을 받고 알람매니저를 호출
    private fun dialogAlarm() {

        val alarmGroup = arrayOf(
            getString(R.string.alarm_second_5),
            getString(R.string.alarm_day_before),
            getString(R.string.alarm_today)
        )
        var selectedAlarm = 0
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alarm_title))
            .setSingleChoiceItems(alarmGroup, selectedAlarm) { dialog, which ->
                selectedAlarm = which
            }
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(getString(R.string.complete)) { dialog, which ->
                when (selectedAlarm) {
                    0 -> Toast.makeText(requireContext(), getString(R.string.alarm_second_5) + getString(R.string.alarm_selected), Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), getString(R.string.alarm_day_before) + getString(R.string.alarm_selected), Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), getString(R.string.alarm_today) + getString(R.string.alarm_selected), Toast.LENGTH_SHORT).show()
                }
                setAlarmReceiver()
            }
            .show()
    }


    //선물하기 버튼 함수
    private fun giftListener() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.gift))
        builder.setMessage(getString(R.string.gift_shop))
        builder.setIcon(R.drawable.ic_gift_grey)

        val btnListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gift.kakao.com"))
                    startActivity(intent)
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com"))
                    startActivity(intent)
                }
            }
        }
        builder.setPositiveButton(getString(R.string.gift_shop_kakao), btnListener)
        builder.setNegativeButton(getString(R.string.gift_shop_coupang), btnListener)
        builder.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactDetailFragment.
         */
        // TODO: Rename and change types and number of parameters

        //        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ContactDetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }

        @JvmStatic
        fun newInstance(user: User) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }



    override fun onStart() {
        super.onStart()
        Log.d("ContactDetailFragment", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ContactDetailFragment", "onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ContactDetailFragment", "onStop()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ContactDetailFragment", "onPause()")
    }
}