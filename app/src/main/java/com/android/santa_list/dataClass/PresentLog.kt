package com.android.santa_list.dataClass

import java.time.LocalDateTime

class PresentLog(val id: Int,
                 val from: User,
                 val to :User,
                 val present: Present,
                 val createdDate: LocalDateTime = LocalDateTime.now(),
                 val updatedDateTime: LocalDateTime = LocalDateTime.now()) {
}