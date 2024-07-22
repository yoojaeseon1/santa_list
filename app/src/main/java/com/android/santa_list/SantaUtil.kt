package com.android.santa_list

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

}