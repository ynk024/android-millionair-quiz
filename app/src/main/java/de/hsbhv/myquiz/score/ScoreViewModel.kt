package de.hsbhv.myquiz.score

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.hsbhv.myquiz.database.Game
import de.hsbhv.myquiz.database.GameDatabaseDao
import kotlinx.coroutines.*

class ScoreViewModel(
    val database: GameDatabaseDao,
    private val wonCash: Int,
    private val correctAnswers: Int,
    private val numQuestions: Int,
    private val timePlayed: Long,
    private val hasTimeLimit: Boolean
) : ViewModel() {
    private val _cash = MutableLiveData<Int>()
    val cash: LiveData<Int>
        get() = _cash

    private val _navigateToGame = MutableLiveData<Boolean>()
    val navigateToGame: LiveData<Boolean>
        get() = _navigateToGame

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val saveResultButtonVisible = if (hasTimeLimit) View.VISIBLE else View.INVISIBLE


//    private var lastGame = MutableLiveData<Game?>()

    init {
        _cash.value = wonCash
    }

    fun onSaveGame(nickname: String, avatarIndex: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val currentGame = Game(
                    nickname = nickname,
                    cash = wonCash,
                    correctQuestions = correctAnswers,
                    questions = numQuestions + 1,
                    timePlayedMilli = timePlayed,
                    avatar = avatarIndex
                )
                database.insert(currentGame)
            }
            _showSnackbarEvent.value = true
        }
    }

    fun onPlayAgain() {
        _navigateToGame.value = true
    }

    fun doneNavigating() {
        _navigateToGame.value = null
    }

    fun onBackHome() {
        _navigateToHome.value = true
    }

    fun doneBackNavigating() {
        _navigateToHome.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}