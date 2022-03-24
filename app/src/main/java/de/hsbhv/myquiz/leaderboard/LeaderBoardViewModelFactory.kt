package de.hsbhv.myquiz.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hsbhv.myquiz.database.GameDatabaseDao
import de.hsbhv.myquiz.score.ScoreViewModel

class LeaderBoardViewModelFactory(private val database: GameDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderBoardViewModel::class.java)) {
            return LeaderBoardViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}