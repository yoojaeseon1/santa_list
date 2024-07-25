package com.android.santa_list.dataClass

import com.android.santa_list.R
import java.time.LocalDateTime

object Dummy {

    val presents: MutableList<Present> = mutableListOf()
    val presentLogs: MutableList<PresentLog> = mutableListOf()
    val myData = mutableListOf<MyData>()
    // jaeseon이 로그인 되어 있다고 가정
    
    val jaeseon = User("id1", "유재선", "010-1234-5678", "jaeseon@gmail.com",UserGroup.NONE, R.drawable.image_jaesun)
    val hwamin = User("id2", "이화민", "010-1256-5678", "hwamin@gmail.com", UserGroup.SCHOOL, R.drawable.image_hwamin)
    val hyehyun = User("id3", "정혜현", "010-1234-5611", "hyehyun@gmail.com", UserGroup.SCHOOL,R.drawable.image_hyehyun)
    val bora = User("id4", "김보라", "010-1255-5655", "bora@gmail.com", UserGroup.SCHOOL, R.drawable.image_bora)
    val ingi = User("id5", "조인기", "010-1266-4567", "ingi@gmail.com", UserGroup.SCHOOL, R.drawable.image_ingi)

    var loginedUser = jaeseon

    val present1 = Present("케이크", R.drawable.image_chulsu)
    val present2 = Present("편지", R.drawable.image_yoori)
    val present3 = Present("초코케이크", R.drawable.image_ddoong)
    val present4 = Present("골드바", R.drawable.image_jjanggu)
    val present5 = Present("가습기", R.drawable.image_hoon)
    val present6 = Present("휴지")
    val present7 = Present("피규어")
    val present8 = Present("아이패드")
    val present9 = Present("캣타워")
    val addPresent = Present("선물추가", R.drawable.image_add_present)

    val MyData = MyData("drawable://" + "${R.drawable.image_choco}", "default_name", "default_phone_number", "default_email", "default_gift_date")

    init {

        presents.add(present1)
        presents.add(present2)
        presents.add(present3)
        presents.add(present4)
        presents.add(present5)
//        presents.add(present6)
//        presents.add(present7)
//        presents.add(present8)
//        presents.add(present9)

        hwamin.wish_list.add(present1)
        hwamin.wish_list.add(present2)
        hwamin.wish_list.add(present3)
        hwamin.wish_list.add(present4)
        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)
//        hwamin.wish_list.add(present5)

//        jaeseon.contacts.add(hwamin)
//        jaeseon.contacts.add(hyehyun)
//        jaeseon.contacts.add(bora)
//        jaeseon.contacts.add(ingi)
//
//        hwamin.contacts.add(jaeseon)
//        hwamin.contacts.add(hyehyun)
//        hwamin.contacts.add(bora)
//        hwamin.contacts.add(ingi)
//
//        hyehyun.contacts.add(jaeseon)
//        hyehyun.contacts.add(hwamin)
//        hyehyun.contacts.add(bora)
//        hyehyun.contacts.add(ingi)
//
//        bora.contacts.add(jaeseon)
//        bora.contacts.add(hwamin)
//        bora.contacts.add(hyehyun)
//        bora.contacts.add(ingi)
//
//        ingi.contacts.add(jaeseon)
//        ingi.contacts.add(hwamin)
//        ingi.contacts.add(hyehyun)
//        ingi.contacts.add(bora)

        presentLogs.add(PresentLog(jaeseon, hwamin, present1, created_date =  LocalDateTime.of(2022,5,1,4,0,0)))
        presentLogs.add(PresentLog(jaeseon, hwamin, present3, created_date =  LocalDateTime.of(2022,5,1,4,0,0)))
        presentLogs.add(PresentLog(jaeseon, hyehyun, present2, created_date =  LocalDateTime.of(2023,1,2,4,0,0)))
        presentLogs.add(PresentLog(jaeseon, bora, present3, created_date =  LocalDateTime.of(2024,2,3,4,0,0)))
        presentLogs.add(PresentLog(jaeseon, ingi, present4, created_date =  LocalDateTime.of(2022,3,4,4,0,0)))
        presentLogs.add(PresentLog(hwamin, jaeseon, present5, created_date =  LocalDateTime.of(2020,4,5,0,0,0)))
        presentLogs.add(PresentLog(hwamin, hyehyun, present7, created_date =  LocalDateTime.of(2021,5,6,4,0,0)))
        presentLogs.add(PresentLog(hwamin, bora, present6, created_date =  LocalDateTime.of(2019,6,7,4,0,0)))
        presentLogs.add(PresentLog(hwamin, ingi, present2, created_date =  LocalDateTime.of(2018,7,8,4,0,0)))
        presentLogs.add(PresentLog(hyehyun, jaeseon, present3, created_date =  LocalDateTime.of(2022,8,9,4,0,0)))
        presentLogs.add(PresentLog(hyehyun, hwamin, present2, created_date =  LocalDateTime.of(2023,9,10,4,0,0)))
        presentLogs.add(PresentLog(hyehyun, bora, present5, created_date =  LocalDateTime.of(2023,10,11,4,0,0)))
        presentLogs.add(PresentLog(hyehyun, ingi, present6, created_date =  LocalDateTime.of(2020,11,12,4,0,0)))
        presentLogs.add(PresentLog(bora, jaeseon, present7, created_date =  LocalDateTime.of(2019,12,13,4,0,0)))
        presentLogs.add(PresentLog(bora, hwamin, present2, created_date =  LocalDateTime.of(2020,1,14,4,0,0)))
        presentLogs.add(PresentLog(bora, hyehyun, present4, created_date =  LocalDateTime.of(2022,2,15,4,0,0)))
        presentLogs.add(PresentLog(bora, ingi, present7, created_date =  LocalDateTime.of(2023,3,16,4,0,0)))
        presentLogs.add(PresentLog(ingi, jaeseon, present9, created_date =  LocalDateTime.of(2024,4,17,4,0,0)))
        presentLogs.add(PresentLog(ingi, hwamin, present1, created_date =  LocalDateTime.of(2023,5,18,4,0,0)))
        presentLogs.add(PresentLog(ingi, hyehyun, present3, created_date =  LocalDateTime.of(2022,6,19,4,0,0)))
        presentLogs.add(PresentLog(ingi, bora, present5, created_date =  LocalDateTime.of(2022,7,20,4,0,0)))

        myData.add(MyData)

    }

}