package com.android.santa_list


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentContactDetailBinding
import com.android.santa_list.repository.PresentLogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.parcelize.Parcelize


private const val ARG_PARAM1 = "param1"

private const val ARG_SELECTED_ALARM = "selectedAlarm"


@Parcelize
class ContactDetailFragment : Fragment(), Parcelable {

    var bestFriend = false
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val presentLogRepository = PresentLogRepository()
    private val santaUtil = SantaUtil.getInstance()
    private val calendar = Calendar.getInstance()
    private var selectedAlarm = 0

//    private val callingListener: CustomClickListener? = null
    private val customClickListener: CustomClickListener by lazy {
        CustomClickListener(requireActivity())
    }

    private val back_pressed_call_back = object : OnBackPressedCallback(true) {

        @SuppressLint("NotifyDataSetChanged")
        override fun handleOnBackPressed() {

            val fragments = requireActivity().supportFragmentManager.fragments
            var contact_list_fragment = ContactListFragment()

            for (fragment in fragments) {
                if(fragment is ContactListFragment) {
                    contact_list_fragment = fragment
                    break
                }
            }

            if(isEnabled) {
                isEnabled = false
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .remove(this@ContactDetailFragment)
                    .commit()
                contact_list_fragment.initRecyclerView(Dummy.dummy_users)
                contact_list_fragment.isStarredList()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    val receivedPresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    val givePresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    val wishPresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }


    private var friend = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friend = it.getParcelable(ARG_PARAM1, User::class.java)?:User()
            selectedAlarm = it.getInt(ARG_SELECTED_ALARM)
        }

//        callingListener?.onClick = santaUtil.callListener
//        callingListener?.onClick(requireActivity(), friend.phone_number)

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

        if(friend.is_starred) {
            binding.detailIvFavorite.setImageResource(R.drawable.icon_star)
            bestFriend = true
        } else {
            binding.detailIvFavorite.setImageResource(R.drawable.icon_empt_star)
            bestFriend = false
        }

        //해당 친구 정보 불러오기
        initFriendData()

            //선물하기버튼 : 클릭 시 다이얼로그 응답에 따라 카카오톡, 쿠팡으로 이동
        _binding?.detailIvGift?.setOnClickListener {
            btnGiftAnimation()
            Handler(Looper.getMainLooper()).postDelayed({
                val giftShopDialogFragment = GiftShopDialogFragment()
                giftShopDialogFragment.show(requireFragmentManager(), "DialogFragment")
            }, 180)
        }

        //툴바버튼 : 클릭 시 친구정보 편집
        _binding?.toolbar?.action?.setOnClickListener {
            val addContactDialog = AddContactDialogFragment.newInstance(friend!!)
            addContactDialog.show(requireFragmentManager(), "DialogFragment")
        }
        //즐겨찾기버튼 : 클릭 시 즐겨찾기 친구로 등록
        _binding?.detailIvFavorite?.setOnClickListener {
            if (bestFriend) {
                _binding?.detailIvFavorite?.setImageResource(R.drawable.icon_empt_star)
                friend.is_starred = false
                bestFriend = false
            } else {
                _binding?.detailIvFavorite?.setImageResource(R.drawable.icon_star)
                friend.is_starred = true
                bestFriend = true
            }
        }

        //알림버튼 : 세가지 기능 중 사용자 상태에 따라 실행 (알림 권한 요청 / 알림 취소 / 알림 예약)
        _binding?.detailCbAlert?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificationManagerCompat.from(
                    requireContext()
                ).areNotificationsEnabled()
            )
                requireAlarmPermission()
            else if (selectedAlarm == 4) cancelAlarm()
            else {
                isCheck()
                val alertDialog = AlertDialogFragment.newInstance(friend)
                alertDialog.isCancelable = false
                alertDialog.show(requireFragmentManager(), "DialogFragment")
            }

        }


        binding.detailTvName.text = friend.name
        binding.detailTvSetPhoneNumber.text = friend.phone_number
        binding.detailTvSetEmail.text = friend.email
        binding.detailTvSetPresentDate.text =
            santaUtil.makeDateFormat(friend.event_date)
        val receivedPresents =
            presentLogRepository.selectPresentList(friend, Dummy.loggedInUser)

        receivedPresentAdapter.imageClick =
            object : PresentListAdapter.ImageClick {
                override fun onClick() {
                    val presentAddFragment =
                        PresentAddFragment.newInstance(
                            friend,
                            "received",
                            this@ContactDetailFragment
                        )
                    presentAddFragment.show(
                        requireActivity().supportFragmentManager,
                        "addPresentDialog"
                    )
                }
            }
        receivedPresentAdapter.submitList(receivedPresents.makePresentList())


        val givePresents =
            presentLogRepository.selectPresentList(Dummy.loggedInUser, friend)
        givePresentAdapter.imageClick =
            object : PresentListAdapter.ImageClick {
                override fun onClick() {

                    val presentAddFragment =
                        PresentAddFragment.newInstance(
                            friend,
                            "give",
                            this@ContactDetailFragment
                        )

                    presentAddFragment.show(
                        requireActivity().supportFragmentManager, "addPresentDialog"
                    )
                }
            }
        givePresentAdapter.submitList(givePresents.makePresentList())

        val wishList = friend.wish_list
        wishPresentAdapter.imageClick =
            object : PresentListAdapter.ImageClick {
                override fun onClick() {

                    val presentAddFragment =
                        PresentAddFragment.newInstance(
                            friend,
                            "wish",
                            this@ContactDetailFragment
                        )

                    presentAddFragment.show(
                        requireActivity().supportFragmentManager, "addPresentDialog"
                    )
                }
            }
        wishPresentAdapter.submitList(wishList.makePresentList())





//        binding.detailBtnMessage.setOnClickListener {
//            val smsUri = Uri.parse("smsto:" + friend.phone_number)
//            val intent = Intent(Intent.ACTION_SENDTO)
//            intent.setData(smsUri)
//            intent.putExtra("sms_body", "")
//            startActivity(intent)
//        }

//        binding.detailBtnCall.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.CALL_PHONE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.CALL_PHONE),
//                    1
//                )
//            } else {
//                val phone_number =
//                    "tel:" + santaUtil.removePhoneHyphen(friend.phone_number)
//                val intent = Intent(
//                    "android.intent.action.CALL",
//                    Uri.parse(phone_number)
//                )
//                startActivity(intent)
//            }
//        }

        customClickListener.phoneNumber = friend.phone_number
        binding.detailBtnMessage.setOnClickListener(customClickListener.messageListener)
        binding.detailBtnCall.setOnClickListener(customClickListener.callingListener)

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), back_pressed_call_back)
    }

    //정보 입력해주는 함수
    fun initFriendData() {
        with(binding)
        {detailTvName.text = friend?.name
        detailTvSetPhoneNumber.text = friend?.phone_number
        detailTvSetEmail.text = friend?.email
            detailRecyclerViewPresentHistoryMine.adapter = receivedPresentAdapter
            detailRecyclerViewPresentHistory.adapter = givePresentAdapter
            detailRecyclerWishPresent.adapter = wishPresentAdapter
            detailTvSetPresentDate.text = santaUtil.makeDateFormat(friend.event_date)
    }
    if(friend.profile_image_uri != "") binding.detailIvProfile.setImageURI(friend.profile_image_uri.toUri())
        else binding.detailIvProfile.setImageResource(friend.profile_image)
    }

    //선물버튼 애니메이션 함수
    fun btnGiftAnimation() {
        val scaleOut = AnimationUtils.loadAnimation(requireContext(), R.anim.ainm_detail_gift)
        _binding?.detailIvGift?.startAnimation(scaleOut)
    }

    //클릭 유무에 맞게 알림버튼을 변경해주는 함수
    private fun isNotCheck() {
        binding.detailCbAlert.isChecked = false
        binding.detailCbAlert.text = getString(R.string.alert_off)
    }
    private fun isCheck() {
        binding.detailCbAlert.isChecked = true
        binding.detailCbAlert.text = getString(R.string.alert_on)
    }

    //알림권한 허가를 요청하는 함수
    private fun requireAlarmPermission() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.detail_dialog_alert
        )
            .setTitle(getString(R.string.alarm_ask_permission))
            .setNegativeButton(getString(R.string.move)) { dialog, which ->
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.cancel)) { dialog, which ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alarm_need_permission),
                    Toast.LENGTH_LONG
                ).show()
            }
            .show()
        isNotCheck()
    }

    //알림을 삭제하는 함수(UI만)
    private fun cancelAlarm() {
        MaterialAlertDialogBuilder(
            requireContext(), R.style.detail_dialog_alert
        )
            .setCancelable(false)
            .setTitle(getString(R.string.alarm_ask_delete))
            .setNegativeButton(getString(R.string.delete)) { dialog, which ->
                isNotCheck()
                selectedAlarm = 0
            }
            .setPositiveButton(getString(R.string.dialog_cancle)) { dialog, which ->
                isCheck()
            }
            .show()
    }
    //사용자가 선택한 항목대로 알릴 시간을 설정하는 함수
    private fun setAlarm() {
        isCheck()
        when (selectedAlarm) {
            //5초뒤
            1 -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alarm_second_5) + getString(R.string.alarm_selected),
                    Toast.LENGTH_SHORT
                ).show()
                reserveAlarm()
                selectedAlarm = 4
            }
            //하루전
            2 -> {
                val alarm_date = friend.event_date.minusDays(1)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alarm_day_before) + getString(R.string.alarm_selected),
                    Toast.LENGTH_SHORT
                ).show()
                calendar.apply {
                    set(Calendar.YEAR, alarm_date.year)
                    set(Calendar.MONTH, alarm_date.monthValue - 1) //0부터 시작한다
                    set(Calendar.DAY_OF_MONTH, alarm_date.dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0) //24시간으로 지정한다
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                reserveAlarm()
                selectedAlarm = 4
            }
            //당일
            3 -> {
                val alarm_date = friend.event_date
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alarm_today) + getString(R.string.alarm_selected),
                    Toast.LENGTH_SHORT
                ).show()
                calendar.apply {
                    set(Calendar.YEAR, alarm_date.year)
                    set(Calendar.MONTH, alarm_date.monthValue -1) //0부터 시작한다
                    set(Calendar.DAY_OF_MONTH, alarm_date.dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0) //24시간으로 지정한다
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                reserveAlarm()
                selectedAlarm = 4
            }
            4 -> {}
            else -> {
                isNotCheck()
                selectedAlarm = 0
            }
        }
    }



    //알람리시버에 알림을 예약하는 함수
    @SuppressLint("ScheduleExactAlarm")
    private fun reserveAlarm() {
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis, pendingIntent
        )
    }



    companion object {

        @JvmStatic
        fun newInstance(user: User, param3: Int) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                    putInt(ARG_SELECTED_ALARM, param3)
                }
            }


        @JvmStatic
        fun newInstance(user: User) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        setAlarm()
        initFriendData()
        val parentActivity = requireActivity() as ContactActivity
        parentActivity.hideViewPager()
    }
}