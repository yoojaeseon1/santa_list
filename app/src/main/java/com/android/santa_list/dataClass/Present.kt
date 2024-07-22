package com.android.santa_list.dataClass

import java.time.LocalDateTime

data class Present(val id: Int,
                   val name: String,
                   val createdDate: LocalDateTime = LocalDateTime.now(),
                   val updatedDateTime: LocalDateTime = LocalDateTime.now())
