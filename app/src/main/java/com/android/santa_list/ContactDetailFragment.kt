package com.android.santa_list

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.NOTIFICATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.User
import com.android.santa_list.databinding.FragmentContactDetailBinding
import com.android.santa_list.repository.PresentLogRepository
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

//    fun getReceivedPresentAdapter(): PresentListAdapter {
//        return this.receivedPresentAdapter
//    }
//
//    fun getGivePresentAdapter(): PresentListAdapter{
//        return this.givePresentAdapter
//    }
//
//    fun getWishPresentAdapter(): PresentListAdapter{
//        return this.wishPresentAdapter
//    }




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

//        알림버튼-------공사중
        _binding?.detailIvAlert?.setOnClickListener {
//            val intent = Intent(this.requireContext(), ContactDetailFragment::class.java)
//            val pendingIntent = PendingIntent.getActivity(this.requireContext(), 0, intent, 0)
            val manager =
                requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            //버전체크
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                //채널 등록
                manager.createNotificationChannel(channel)
                //채널을 이용하여 빌더 생성
                builder = NotificationCompat.Builder(this.requireContext(), channelId)
            } else {
                //버전 이하
                builder = NotificationCompat.Builder(this.requireContext())
            }
            builder.run {
                setSmallIcon(R.drawable.ic_alert_on)
                setWhen(System.currentTimeMillis())
                setContentTitle(getString(R.string.christmas_eve))
                setContentText(getString(R.string.christmas))
//                setContentIntent(pendingIntent)
                setAutoCancel(true)



            }
            manager.notify(1, builder.build())
        }

       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           if (!NotificationManagerCompat.from(this.requireContext()).areNotificationsEnabled()) {
               val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                   putExtra(Settings.EXTRA_APP_PACKAGE, `package`)
               }
               startActivity(intent)
           }
       }

        val receivedPresents = presentLogRepository.selectPresentList(friend!!, Dummy.loginedUser)
        receivedPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
//                val presentAddFragment = PresentAddFragment()

                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "received",this@ContactDetailFragment)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.frame_layout, presentAddFragment)
//                    .addToBackStack(null)
//                    .commit()
                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog"
                )
            }
        }
        receivedPresentAdapter.submitList(santaUtil.makePresentList(receivedPresents))


        val givePresents = presentLogRepository.selectPresentList(Dummy.loginedUser, friend!!)
        givePresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
//                val presentAddFragment = PresentAddFragment()
                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "give", this@ContactDetailFragment)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.frame_layout, presentAddFragment)
//                    .addToBackStack(null)
//                    .commit()
                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog")
            }
        }
        givePresentAdapter.submitList(santaUtil.makePresentList(givePresents))

        val wishList = friend!!.wish_list
        wishPresentAdapter.imageClick = object : PresentListAdapter.ImageClick {
            override fun onClick() {
//                val presentAddFragment = PresentAddFragment()
                val presentAddFragment = PresentAddFragment.newInstance(friend!!, "wish", this@ContactDetailFragment)

//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.frame_layout, presentAddFragment)
//                    .addToBackStack(null)
//                    .commit()
                presentAddFragment.show(
                    requireActivity().supportFragmentManager, "addPresentDialog")
            }
        }
        wishPresentAdapter.submitList(santaUtil.makePresentList(wishList))

        binding.detailRecyclerViewSonjulGo.adapter = receivedPresentAdapter
        binding.detailRecyclerViewPresentHistory.adapter = givePresentAdapter
        binding.detailRecyclerWishPresent.adapter = wishPresentAdapter
        
//        알림버튼 함수-------공사중
//        fun btnNotificationListener() {
//            val manager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            val builder: NotificationCompat.Builder
//            //버전체크
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channelId = "one-channel"
//                val channelName = "My Channel One"
//                val channel = NotificationChannel(
//                    channelId,
//                    channelName,
//                    NotificationManager.IMPORTANCE_DEFAULT
//                )
//                //채널 등록
//                manager.createNotificationChannel(channel)
//                //채널을 이용하여 빌더 생성
//                builder = NotificationCompat.Builder(requireContext(), channelId)
//            } else {
//                //버전 이하
//                builder = NotificationCompat.Builder(requireContext())
//            }
//            builder.run {
//                setSmallIcon(R.drawable.ic_alert_on)
//                setWhen(System.currentTimeMillis())
//                setContentTitle(getString(R.string.christmas))
//                setContentText(getString(R.string.christmas))
//            }
//            manager.notify(1, builder.build())
//        }
//
//        binding.detailBtnMessage.setOnClickListener {
//            Log.d(TAG,"클릭")
//            btnNotificationListener()
//        }


        //선물하기버튼 : 클릭 시 다이얼로그 응답에 따라 카카오톡, 쿠팡으로 이동
        _binding?.detailIvGift?.setOnClickListener {
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

        binding.detailBtnMessage.setOnClickListener {
            val smsUri = Uri.parse("smsto:" + friend?.phone_number)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData(smsUri)
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }

        binding.detailBtnCall.setOnClickListener {

            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
            else {
                val phone_number = "tel:" + santaUtil.removePhoneHyphen(friend!!.phone_number)
                val intent = Intent("android.intent.action.CALL", Uri.parse(phone_number))
                startActivity(intent)
            }
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