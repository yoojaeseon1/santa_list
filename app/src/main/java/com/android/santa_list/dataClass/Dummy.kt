package com.android.santa_list.dataClass

import com.android.santa_list.R
import java.time.LocalDateTime

object Dummy {
    fun dummyUserList(): MutableList<User> {
        return mutableListOf(
            jaeseon,
            hwamin,
            hyehyun,
            bora,
            ingi,
            User("id6", "초코", "010-1234-5678", "choco_love@gmail.com", UserGroup.FAMILY, R.drawable.image_choco, is_starred = false),
            User("id7", "훈이", "010-1234-5678", "hooon@gmail.com", UserGroup.FRIEND, R.drawable.image_hoon, is_starred = false),
            User("id8", "뚱이", "010-1234-5678", "ddongddong2@gmail.com", UserGroup.NONE, R.drawable.image_ddoong, is_starred = false),
            User("id9", "철수", "010-1234-5678", "chulsoooo@gmail.com", UserGroup.FRIEND, R.drawable.image_chulsu, is_starred = false),
            User("id10", "흰둥이", "010-1234-5678", "white_dog@gmail.com", UserGroup.FAMILY, R.drawable.image_huindoong, is_starred = false),
            User("id11", "짱구", "010-1234-5678", "crayon@gmail.com", UserGroup.FRIEND, R.drawable.image_jjanggu, is_starred = false),
            User("id12", "스폰지밥", "010-1234-5678", "sponge@gmail.com", UserGroup.NONE, R.drawable.image_spongebob, is_starred = false),
            User("id13", "유리", "010-1234-5678", "glass@gmail.com", UserGroup.FRIEND, R.drawable.image_yoori, is_starred = false),
            User("id14", "피카츄", "010-1234-5678", "thunder@gmail.com", UserGroup.FAMILY, R.drawable.image_pikachu, is_starred = false),
        )
    }

    val presents: MutableList<Present> = mutableListOf()
    val presentLogs: MutableList<PresentLog> = mutableListOf()
    
    val myData = mutableListOf<MyData>()

    val jaeseon = User("id1", "유재선", "010-1234-5678", "jaeseon@gmail.com",UserGroup.SCHOOL, R.drawable.image_jaesun, is_starred = false)
    val hwamin = User("id2", "이화민", "010-1256-5678", "hwamin@gmail.com", UserGroup.SCHOOL, R.drawable.image_hwamin, is_starred = false)
    val hyehyun = User("id3", "정혜현", "010-1234-5611", "hyehyun@gmail.com", UserGroup.SCHOOL,R.drawable.image_hyehyun, is_starred = false)
    val bora = User("id4", "김보라", "010-1255-5655", "bora@gmail.com", UserGroup.SCHOOL, R.drawable.image_bora, is_starred = false)
    val ingi = User("id5", "조인기", "010-1266-4567", "ingi@gmail.com", UserGroup.SCHOOL, R.drawable.image_ingi, is_starred = false)

    var loggedInUser = jaeseon

//    val present1 = Present("케이크", R.drawable.image_chulsu)
//    val present2 = Present("편지", R.drawable.image_yoori)
//    val present3 = Present("초코케이크", R.drawable.image_ddoong)
//    val present4 = Present("골드바", R.drawable.image_jjanggu)
//    val present5 = Present("가습기", R.drawable.image_hoon)
//    val present6 = Present("휴지")
//    val present7 = Present("피규어")
//    val present8 = Present("아이패드")
//    val present9 = Present("캣타워")
//    val addPresent = Present("선물추가", R.drawable.image_add_present)

    val MyData = MyData("", "", "", "", arrayOf(null, null, null))

    init {

//        presents.add(present1)
//        presents.add(present2)
//        presents.add(present3)
//        presents.add(present4)
//        presents.add(present5)
//        presents.add(present6)
//        presents.add(present7)
//        presents.add(present8)
//        presents.add(present9)

//        hwamin.wish_list.add(present1)
//        hwamin.wish_list.add(present2)
//        hwamin.wish_list.add(present3)
//        hwamin.wish_list.add(present4)
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

//        presentLogs.add(PresentLog(jaeseon, hwamin, present1, created_date =  LocalDateTime.of(2022,5,1,4,0,0)))
//        presentLogs.add(PresentLog(jaeseon, hwamin, present3, created_date =  LocalDateTime.of(2022,5,1,4,0,0)))
//        presentLogs.add(PresentLog(jaeseon, hyehyun, present2, created_date =  LocalDateTime.of(2023,1,2,4,0,0)))
//        presentLogs.add(PresentLog(jaeseon, bora, present3, created_date =  LocalDateTime.of(2024,2,3,4,0,0)))
//        presentLogs.add(PresentLog(jaeseon, ingi, present4, created_date =  LocalDateTime.of(2022,3,4,4,0,0)))
//        presentLogs.add(PresentLog(hwamin, jaeseon, present5, created_date =  LocalDateTime.of(2020,4,5,0,0,0)))
//        presentLogs.add(PresentLog(hwamin, hyehyun, present7, created_date =  LocalDateTime.of(2021,5,6,4,0,0)))
//        presentLogs.add(PresentLog(hwamin, bora, present6, created_date =  LocalDateTime.of(2019,6,7,4,0,0)))
//        presentLogs.add(PresentLog(hwamin, ingi, present2, created_date =  LocalDateTime.of(2018,7,8,4,0,0)))
//        presentLogs.add(PresentLog(hyehyun, jaeseon, present3, created_date =  LocalDateTime.of(2022,8,9,4,0,0)))
//        presentLogs.add(PresentLog(hyehyun, hwamin, present2, created_date =  LocalDateTime.of(2023,9,10,4,0,0)))
//        presentLogs.add(PresentLog(hyehyun, bora, present5, created_date =  LocalDateTime.of(2023,10,11,4,0,0)))
//        presentLogs.add(PresentLog(hyehyun, ingi, present6, created_date =  LocalDateTime.of(2020,11,12,4,0,0)))
//        presentLogs.add(PresentLog(bora, jaeseon, present7, created_date =  LocalDateTime.of(2019,12,13,4,0,0)))
//        presentLogs.add(PresentLog(bora, hwamin, present2, created_date =  LocalDateTime.of(2020,1,14,4,0,0)))
//        presentLogs.add(PresentLog(bora, hyehyun, present4, created_date =  LocalDateTime.of(2022,2,15,4,0,0)))
//        presentLogs.add(PresentLog(bora, ingi, present7, created_date =  LocalDateTime.of(2023,3,16,4,0,0)))
//        presentLogs.add(PresentLog(ingi, jaeseon, present9, created_date =  LocalDateTime.of(2024,4,17,4,0,0)))
//        presentLogs.add(PresentLog(ingi, hwamin, present1, created_date =  LocalDateTime.of(2023,5,18,4,0,0)))
//        presentLogs.add(PresentLog(ingi, hyehyun, present3, created_date =  LocalDateTime.of(2022,6,19,4,0,0)))
//        presentLogs.add(PresentLog(ingi, bora, present5, created_date =  LocalDateTime.of(2022,7,20,4,0,0)))

        myData.add(MyData)

    }

}