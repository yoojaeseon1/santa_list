package com.android.santa_list

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.Present
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * TODO
 *
 * util관련 함수를 모은 클래스
 *
 * 날짜 형식 변환
 * 번호 등록 시 정보 validation 등
 *
 */
class SantaUtil {

    companion object{

        private var santaUtil: SantaUtil? = null

        @Synchronized
        fun getInstance() : SantaUtil{
            if(santaUtil == null) {
                santaUtil = SantaUtil()
            }
            return santaUtil as SantaUtil
        }
    }


    /**
     * TODO
     *
     * LocalDateTime 인스턴스를 화면 출력 형식에 맞는 String으로 변환
     * 
     * @param date : 변환할 LocalDateTime instance
     * @return 변환된 날짜(형식 : 0000년 0월 00일)
     */
    fun makeDateFormat(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 M월 dd일"))
    }

    fun removePhoneHyphen(phone_number: String): String{
        return phone_number.replace("-","")
    }

    fun makePresentList(presents: MutableList<Present>): MutableList<Present>{
//        if(presents.size <= 7) {
//            val subList = presents.toMutableList()
////            subList.add(Dummy.addPresent)
//
//            if(subList.size == 5)
//                subList.removeAt(subList.size - 2)
//
//            return subList
//        } else {
//            val subList = presents.subList(0, 7)
////            subList.add(Dummy.addPresent)
//
//            return subList
//        }
        var subList = mutableListOf<Present>()
        subList.add(Present(""))

        if(presents.size >= 8)
            subList.addAll(presents.subList(0,6))
        else
            subList.addAll(presents)
//            subList = presents.toMutableList()

//        subList.add(0, Present(""))

        return subList

    }

    fun getRealPathFromURI(activity: Activity, uri: Uri): String {
        var index = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity.contentResolver.query(uri, proj, null, null, null)
        if(cursor == null)
            return "empty uri"

        if(cursor.moveToFirst()){
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(index)
    }

}