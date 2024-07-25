package com.android.santa_list.dataClass

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * TODO
 *
 * 사용자(앱 사용자 / 등록될 사람) 정보
 *
 * @property id
 * @property phone_number
 * @property email
 * @property profile_image imageResourceId
 * @property my_contacts 내 연락처 목록
 * @property wish_list 원하는 선물 목록
 * @property event_date 선물 해줄 날짜
 * @property created_date 계정 생성일
 * @property updated_date 계정 정보 수정일
 */
@Parcelize
data class User(val id: String,
                var name: String,
                var phone_number: String,
                var email: String,
                var group: UserGroup,
                var profile_image: Int,
                var is_starred: Boolean = false,
                val contacts: MutableList<User> = mutableListOf(),
                val wish_list: MutableList<Present> = mutableListOf(),
                var event_date: LocalDateTime = LocalDateTime.now(),

                val presented: Boolean = false,
                val willPresent: Boolean = false,

                val created_date: LocalDateTime = LocalDateTime.now(),
                var updated_date: LocalDateTime? = null) : Parcelable

@Parcelize
data class MyData (
    var uri: String?,
    var name: String?,
    var phone_number: String?,
    var email: String?,
    var gift_date: String?
) : Parcelable