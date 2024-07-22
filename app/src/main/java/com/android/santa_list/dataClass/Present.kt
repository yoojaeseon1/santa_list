package com.android.santa_list.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime


/**
 * TODO
 *
 * 선물 품목
 * 
 * @property id
 * @property name 선물 명
 * @property created_date 생성한 날짜
 * @property updated_date 수정한 날짜
 */
@Parcelize
data class Present(var name: String,
                   val id: Int = idCount++,
                   val created_date: LocalDateTime = LocalDateTime.now(),
                   var updated_date: LocalDateTime? = null) : Parcelable

{

    companion object {
        var idCount = 1
    }
}
