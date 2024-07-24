package com.android.santa_list.repository

import com.android.santa_list.dataClass.Dummy
import com.android.santa_list.dataClass.Present
import com.android.santa_list.dataClass.User

class PresentLogRepository {

    private val presentLogs = Dummy.presentLogs

    fun selectPresentList(from: User, to: User): MutableList<Present>{
        val givenPresents = mutableListOf<Present>()

        for (presentLog in presentLogs) {
            if(presentLog.from.id == from.id && presentLog.to.id == to.id)
                givenPresents.add(presentLog.present)
        }

        return givenPresents
    }



}