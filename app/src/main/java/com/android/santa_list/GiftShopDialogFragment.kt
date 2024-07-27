package com.android.santa_list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.android.santa_list.databinding.FragmentGiftShopDialogBinding

class GiftShopDialogFragment : DialogFragment() {
    private var _binding: FragmentGiftShopDialogBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGiftShopDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.giftShopTvKakao?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gift.kakao.com"))
                    startActivity(intent)
        }

        _binding?.giftShopTvCoupang?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coupang.com"))
            startActivity(intent)
        }

        _binding?.giftShopBtnCancle?.setOnClickListener {
            dismiss()
        }
    }
}