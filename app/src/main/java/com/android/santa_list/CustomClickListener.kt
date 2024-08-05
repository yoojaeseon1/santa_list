package com.android.santa_list

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CustomClickListener(val activity: Activity,
                          var phoneNumber: String = "010-1234-5678") {

    val santaUtil = SantaUtil.getInstance()

    val callingListener = { view: View ->
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            val phone_number =
                "tel:" + santaUtil.removePhoneHyphen(phoneNumber)
            val intent = Intent(
                "android.intent.action.CALL",
                Uri.parse(phone_number)
            )
            activity.startActivity(intent)
        }
    }

    val messageListener = {view: View ->
        val smsUri = Uri.parse("smsto:" + phoneNumber)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setData(smsUri)
        intent.putExtra("sms_body", "")
        activity.startActivity(intent)
    }

}