package de.hsbhv.myquiz.leaderboard

import androidx.lifecycle.ViewModel
import de.hsbhv.myquiz.database.GameDatabaseDao

class LeaderBoardViewModel(val database: GameDatabaseDao) : ViewModel() {
    val games = database.getAllGames()
}