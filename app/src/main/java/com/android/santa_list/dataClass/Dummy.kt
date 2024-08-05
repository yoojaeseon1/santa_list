package com.android.santa_list.dataClass

import com.android.santa_list.R

object Dummy {

    val presentLogs: MutableList<PresentLog> = mutableListOf()
    val myData = mutableListOf<MyData>()


    private val jaeseon = User("id1", "유재선", "010-1234-5678", "jaeseon@gmail.com",UserGroup.SCHOOL, R.drawable.image_jaesun, is_starred = false)
    private val hwamin = User("id2", "이화민", "010-1256-5678", "hwamin@gmail.com", UserGroup.SCHOOL, R.drawable.image_hwamin, is_starred = false)
    private val hyehyun = User("id3", "정혜현", "010-1234-5611", "hyehyun@gmail.com", UserGroup.SCHOOL,R.drawable.image_hyehyun, is_starred = false)
    private val bora = User("id4", "김보라", "010-1255-5655", "bora@gmail.com", UserGroup.SCHOOL, R.drawable.image_bora, is_starred = false)
    private val ingi = User("id5", "조인기", "010-1266-4567", "ingi@gmail.com", UserGroup.SCHOOL, R.drawable.image_ingi, is_starred = false)
    private val choco = User("id6", "초코", "010-1234-5678", "choco_love@gmail.com", UserGroup.FAMILY, R.drawable.image_choco, is_starred = false)
    private val hooon = User("id7", "훈이", "010-1234-5678", "hooon@gmail.com", UserGroup.FRIEND, R.drawable.image_hoon, is_starred = false)
    private val ddongddong = User("id8", "뚱이", "010-1234-5678", "ddongddong2@gmail.com", UserGroup.NONE, R.drawable.image_ddoong, is_starred = false)
    private val chulsoooo = User("id9", "철수", "010-1234-5678", "chulsoooo@gmail.com", UserGroup.FRIEND, R.drawable.image_chulsu, is_starred = false)
    private val white_dog = User("id10", "흰둥이", "010-1234-5678", "white_dog@gmail.com", UserGroup.FAMILY, R.drawable.image_huindoong, is_starred = false)
    private val crayon = User("id11", "짱구", "010-1234-5678", "crayon@gmail.com", UserGroup.FRIEND, R.drawable.image_jjanggu, is_starred = false)
    private val sponge = User("id12", "스폰지밥", "010-1234-5678", "sponge@gmail.com", UserGroup.NONE, R.drawable.image_spongebob, is_starred = false)
    private val glass = User("id13", "유리", "010-1234-5678", "glass@gmail.com", UserGroup.FRIEND, R.drawable.image_yoori, is_starred = false)
    private val thunder = User("id14", "피카츄", "010-1234-5678", "thunder@gmail.com", UserGroup.FAMILY, R.drawable.image_pikachu, is_starred = false)

    // jaeseon이 로그인 되어 있다고 가정
    var loggedInUser = jaeseon
  
    val initMyData: MyData = MyData("","", "산타클로스", "010-1225-5221", "santa@gmail.com", arrayOf(null, null, null))

    var dummy_users: MutableList<User> = mutableListOf()

    init {
        dummy_users = mutableListOf(
            jaeseon,
            hwamin,
            hyehyun,
            bora,
            ingi,
            choco,
            hooon,
            ddongddong,
            chulsoooo,
            white_dog,
            crayon,
            sponge,
            glass,
            thunder
        )

        myData.add(initMyData)
    }

}