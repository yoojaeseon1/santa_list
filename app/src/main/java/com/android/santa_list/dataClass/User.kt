package com.android.santa_list.dataClass

data class User(val id: String,
           var phone_number: String,
           var profile_image: Int,
           var email: String,
           val starred_users: MutableList<User>) {



}