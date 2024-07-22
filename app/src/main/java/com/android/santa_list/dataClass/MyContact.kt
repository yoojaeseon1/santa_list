package com.android.santa_list.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * TODO
 *
 * 나한테 즐겨찾기가 되어있는지 확인하기 위한 용도(사용자 별로 즐겨찾기 여부가 다를 수 있으니까)
 * 
 * @property user 전화번호부에 등록될 사용자
 * @property is_starred 즐겨 찾기 여부
 */
@Parcelize
data class MyContact(val user: User,
                     val id: Int = idCount++,
                     var is_starred: Boolean = false) : Parcelable

{
    companion object{
        var idCount = 1
    }
}
