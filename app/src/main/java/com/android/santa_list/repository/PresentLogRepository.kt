package com.android.santa_list.repository

import android.util.Log
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

    fun selectGiveUserList(): MutableList<User>{
        val filteredUser = mutableSetOf<User>()

        for (presentLog in presentLogs) {
            if(presentLog.to == Dummy.loggedInUser)
                filteredUser.add(presentLog.from)
        }

        val filteredList = filteredUser.toMutableList()

        filteredList.sortedWith(object : Comparator<User> {
            override fun compare(o1: User, o2: User): Int {
                return o1.name.compareTo(o2.name)
            }
        })

        return filteredList
    }

    fun selectReceivedUserList(): MutableList<User>{
        val filteredUser = mutableSetOf<User>()

        for (presentLog in presentLogs) {
            if(presentLog.from == Dummy.loggedInUser)
                filteredUser.add(presentLog.to)
        }

        val filteredList = filteredUser.toMutableList()

        filteredList.sortedWith(object : Comparator<User> {
            override fun compare(o1: User, o2: User): Int {
                return o1.name.compareTo(o2.name)
            }
        })

        return filteredList
    }


}