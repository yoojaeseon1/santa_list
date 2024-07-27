package com.android.santa_list


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
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
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentContactDetailBinding
import com.android.santa_list.repository.PresentLogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.parcelize.Parcelize
import java.lang.StringBuilder
import java.time.LocalDateTime


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
const val TAG = "ContactDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@Parcelize
class ContactDetailFragment : Fragment(), Parcelable {
    // TODO: Rename and change types of parameters

    var param2: User? = null
    var bestFriend = false
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val presentLogRepository = PresentLogRepository()
    private val santaUtil = SantaUtil.getInstance()
    private var selectedAlarm = 7

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
            selectedAlarm = it.getInt(ARG_PARAM3)
            friend = it.getParcelable(ARG_PARAM1, User::class.java)?:User()

//            Log.d("7dla?", "${friend}, ${it.getInt(ARG_PARAM3)}")

//            Log.d("받습니다", "${friend}, ${it.getInt(ARG_PARAM3)}")
//            setFragmentResultListener("dataSend") { key, bundle ->
//                friend = it.getParcelable(ARG_PARAM1, User::class.java)
//                setAlarm()
//            }
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

        if(friend.is_starred) {
            binding.detailIvFavorite.setImageResource(R.drawable.icon_star)
            bestFriend = true
        } else {
            binding.detailIvFavorite.setImageResource(R.drawable.icon_empt_star)
            bestFriend = false
        }


        //선물하기버튼 : 클릭 시 다이얼로그 응답에 따라 카카오톡, 쿠팡으로 이동
        _binding?.detailIvGift?.setOnClickListener {
            val giftShopDialogFragment = GiftShopDialogFragment()
            giftShopDialogFragment.show(requireFragmentManager(), "DialogFragment")
        }

        //툴바버튼 : 클릭 시 친구정보 편집
        _binding?.toolbar?.action?.setOnClickListener {
            val addContactDialog = AddContactDialogFragment()
            addContactDialog.show(requireFragmentManager(), "DialogFragment")

        }
        //즐겨찾기버튼 : 클릭 시 즐겨찾기 친구로 등록
        _binding?.detailIvFavorite?.setOnClickListener {
            Log.d("contactDetailFragment", "bestFriend = ${bestFriend}")
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

        //알림버튼 : 5초뒤, 하루전, 당일 중 사용자 입력에 따라 알림 출력
        _binding?.detailCbAlert?.setOnClickListener {
            if (selectedAlarm != null && selectedAlarm != 0) cancelAlarm()
            else {
                val alertDialog = AlertDialogFragment.newInstance(friend)
                alertDialog.show(requireFragmentManager(), "DialogFragment")
            }
        }
        //알림함수 호출




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
        receivedPresentAdapter.submitList(
            santaUtil.makePresentList(
                receivedPresents
            )
        )


        val givePresents =
            presentLogRepository.selectPresentList(Dummy.loggedInUser, friend!!)
        givePresentAdapter.imageClick =
            object : PresentListAdapter.ImageClick {
                override fun onClick() {

                    val presentAddFragment =
                        PresentAddFragment.newInstance(
                            friend!!,
                            "give",
                            this@ContactDetailFragment
                        )

                    presentAddFragment.show(
                        requireActivity().supportFragmentManager, "addPresentDialog"
                    )
                }
            }
        givePresentAdapter.submitList(santaUtil.makePresentList(givePresents))

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
        wishPresentAdapter.submitList(santaUtil.makePresentList(wishList))

        binding.detailRecyclerViewSonjulGo.adapter = receivedPresentAdapter
        binding.detailRecyclerViewPresentHistory.adapter = givePresentAdapter
        binding.detailRecyclerWishPresent.adapter = wishPresentAdapter



        binding.detailBtnMessage.setOnClickListener {
            val smsUri = Uri.parse("smsto:" + friend.phone_number)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData(smsUri)
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }

        binding.detailBtnCall.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("contactDetailFragment", "don't have permission")

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1
                )

            } else {
                Log.d("contactDetailFragment", "have permission")
                val phone_number =
                    "tel:" + santaUtil.removePhoneHyphen(friend.phone_number)
                val intent = Intent(
                    "android.intent.action.CALL",
                    Uri.parse(phone_number)
                )
                startActivity(intent)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), back_pressed_call_back)

    }

    //알림 취소하는 함수
    private fun cancelAlarm() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.gift))
            .setNegativeButton(getString(R.string.dialog_cancle)) { dialog, which ->
            }
            .show()
    }

    //사용자 설정대로 알림 예약하는 함수
    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm() {
        val calendar = Calendar.getInstance()
        val santaDay = arrayOf(2024, 7, 25, 21, 19, 0)
        val alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        if (selectedAlarm == 0 || selectedAlarm == 4 || selectedAlarm == null)
            _binding?.detailCbAlert?.apply {
                setChecked(false)
                text = getString(R.string.alert_off)
            } else {
            when (selectedAlarm) {
                1 -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alarm_second_5) + getString(R.string.alarm_selected),
                        Toast.LENGTH_SHORT
                    ).show()

                    _binding?.detailCbAlert?.apply {
                        isChecked = true
                        text = getString(R.string.alert_off)
                    }

                    val localDateTime = StringBuilder(LocalDateTime.now().toString())
                    val localDateTimeArray = arrayOf("", "", "", "", "", "0")
                    localDateTime.forEachIndexed { index, i ->
                        when (index) {
                            in 0..3 -> localDateTimeArray[0] += i.toString()
                            in 5..6 -> localDateTimeArray[1] += i.toString()
                            in 8..9 -> localDateTimeArray[2] += i.toString()
                            in 11..12 -> localDateTimeArray[3] += i.toString()
                            in 14..15 -> localDateTimeArray[4] += i.toString()
                        }
                    }
                    val now = localDateTimeArray.map { it.toInt() }.toIntArray()
                    calendar.apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.YEAR, now[0])
                        set(Calendar.MONTH, now[1] - 1) //0부터 시작한다
                        set(Calendar.DAY_OF_MONTH, now[2])
                        set(Calendar.HOUR_OF_DAY, now[3]) //24시간으로 지정한다
                        set(Calendar.MINUTE, now[4])
                        set(Calendar.SECOND, now[5] + 30)
                    }
                }
                //하루전
                2 -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alarm_day_before) + getString(R.string.alarm_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                    _binding?.detailCbAlert?.apply {
                        isChecked = true
                        text = getString(R.string.alert_off)
                    }
                    calendar.apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.YEAR, santaDay[0])
                        set(Calendar.MONTH, santaDay[1] - 1) //0부터 시작한다
                        set(Calendar.DAY_OF_MONTH, santaDay[2] - 1)
                        set(Calendar.HOUR_OF_DAY, santaDay[3]) //24시간으로 지정한다
                        set(Calendar.MINUTE, santaDay[4])
                        set(Calendar.SECOND, santaDay[5])
                    }
                }
                //당일
                3 -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.alarm_today) + getString(R.string.alarm_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                    _binding?.detailCbAlert?.apply {
                        isChecked = true
                        text = getString(R.string.alert_off)
                    }
                    calendar.apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.YEAR, santaDay[0])
                        set(Calendar.MONTH, santaDay[1] - 1) //0부터 시작한다
                        set(Calendar.DAY_OF_MONTH, santaDay[2])
                        set(Calendar.HOUR_OF_DAY, santaDay[3]) //24시간으로 지정한다
                        set(Calendar.MINUTE, santaDay[4])
                        set(Calendar.SECOND, santaDay[5])
                    }
                }
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis, pendingIntent
            )
        }

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
        fun newInstance(user: User, param3: Int) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)
                    putInt(ARG_PARAM3, param3)
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

    override fun onStart() {
        super.onStart()
        Log.d("ContactDetailFragment", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        setAlarm()
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

    override fun onDetach() {
        super.onDetach()
        Log.d("ContactDetailFragment", "onDetach()")
    }
}
