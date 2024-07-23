package com.android.santa_list.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserList(

    var image : Int,
    var name: String,
    var is_starred : Boolean = false ) : Parcelable
