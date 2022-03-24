package de.hsbhv.myquiz.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory(private val hasTimeLimit: Boolean, private val isRisiko: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(hasTimeLimit, isRisiko) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}