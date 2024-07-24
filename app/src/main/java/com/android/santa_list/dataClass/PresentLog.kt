package com.android.santa_list.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

/**
 * TODO
 *
 * 주고 받은 선물에 대한 기록
 * 
 * @property id
 * @property from 선물 한 사람
 * @property to 선물 받은 사람
 * @property present 받은 선물
 * @property created_date 선물 한 날짜
 * @property updated_date 선물이 수정된 날짜
 */
@Parcelize
class PresentLog(val from: User,
                 val to: User,
                 val present: Present,
                 val id: Int = idCount++,
                 val created_date: LocalDateTime = LocalDateTime.now(),
                 var updated_date: LocalDateTime? = null) : Parcelable
{
    companion object {
        var idCount = 1
    }

}