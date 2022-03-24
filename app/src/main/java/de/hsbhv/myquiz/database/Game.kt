package de.hsbhv.myquiz.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games_table")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameId: Long = 0L,
    var nickname: String = "",
    var questions: Int = 0,
    var correctQuestions: Int = 0,
    var cash: Int = 0,
    var timePlayedMilli: Long = 0L,
    var avatar: Int = 0
)




