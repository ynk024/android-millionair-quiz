package de.hsbhv.myquiz.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.hsbhv.myquiz.database.GameDatabaseDao

class ScoreViewModelFactory(
    private val database: GameDatabaseDao,
    private val wonCash: Int,
    private val correctAnswers: Int,
    private val numQuestions: Int,
    private val timePlayedMillis: Long,
    private val hasTimeLimit: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(database, wonCash, correctAnswers, numQuestions, timePlayedMillis, hasTimeLimit) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}