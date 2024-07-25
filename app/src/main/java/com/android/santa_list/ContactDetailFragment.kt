package com.android.santa_list

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentContactDetailBinding
import com.android.santa_list.repository.PresentLogRepository

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
class ContactDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val presentLogRepository = PresentLogRepository()
    private val santaUtil = SantaUtil.getInstance()

    private val receivedPresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    private val givePresentAdapter: PresentListAdapter by lazy {
        PresentListAdapter()
    }

    private val wishPresentAdapter: PresentListAdapter by lazy {
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
        _binding?.detailIvAlert?.setOnClickListener {
            initAlarm()
            initNotification()
            dialogAlarm()
              }

        //선물하기버튼 : 클릭 시 다이얼로그 응답에 따라 카카오톡, 쿠팡으로 이동
        _binding?.detailIvGift?.setOnClickListener {


        }



        val receivedPresents = presentLogRepository.selectPresentList(Dummy.loginedUser, friend!!)
        receivedPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
                val presentAddFragment = PresentAddFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, presentAddFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        receivedPresentAdapter.submitList(santaUtil.makePresentList(receivedPresents))


        val givePresents = presentLogRepository.selectPresentList(friend!!, Dummy.loginedUser)
        givePresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
                val presentAddFragment = PresentAddFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, presentAddFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        givePresentAdapter.submitList(santaUtil.makePresentList(givePresents))

        val wishList = friend!!.wish_list
        wishPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
                val presentAddFragment = PresentAddFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, presentAddFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        wishPresentAdapter.submitList(santaUtil.makePresentList(wishList))

        binding.detailRecyclerViewSonjulGo.adapter = receivedPresentAdapter
        binding.detailRecyclerViewPresentHistory.adapter = givePresentAdapter
        binding.detailRecyclerWishPresent.adapter = wishPresentAdapter


    }

    //알람 함수
    @SuppressLint("ScheduleExactAlarm")
    fun initAlarm() {
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
            calendar.timeInMillis, pendingIntent)
    }

    fun dialogAlarm() {
        val builder = AlertDialog.Builder(this.requireContext())
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



    //알림 함수
    private fun initNotification() {
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


}