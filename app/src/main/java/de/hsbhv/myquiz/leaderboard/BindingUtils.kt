package de.hsbhv.myquiz.leaderboard

import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import de.hsbhv.myquiz.R
import de.hsbhv.myquiz.database.Game

@BindingAdapter("timePlayedFormatted")
fun TextView.setTimePlayed(item: Game?){
    item?.let {
        text = DateUtils.formatElapsedTime(item.timePlayedMilli / 1000L)
    }
}

@BindingAdapter("cashString")
fun TextView.setWonCash(item: Game?){
    item?.let {
        text = "${item.cash}€"
    }
}

@BindingAdapter("nicknameString")
fun TextView.setNickname(item: Game?){
    item?.let {
        text = item.nickname
    }
}

@BindingAdapter("questionAnswersFormatted")
fun TextView.setCorrectAnswers(item: Game?){
    item?.let {
        text = "${item.correctQuestions}/${item.questions}"
    }
}



@BindingAdapter("leaderString")
fun TextView.setLeader(item: Game?){
    item?.let {
        text = item.nickname.toString()
    }
}

@BindingAdapter("playerImage")
fun ImageView.setSleepImage(item: Game?) {
    item?.let {
        setImageResource(when (item.avatar) {
            0 -> R.drawable.avatar_1
            1 -> R.drawable.avatar_2
            2 -> R.drawable.avatar_3
            3 -> R.drawable.avatar_4
            4 -> R.drawable.avatar_5
            5 -> R.drawable.avatar_6
            6 -> R.drawable.avatar_7
            7 -> R.drawable.avatar_8
            8 -> R.drawable.avatar_9
            9 -> R.drawable.avatar_10
            10 -> R.drawable.avatar_11
            11 -> R.drawable.avatar_12
            13 -> R.drawable.avatar_13
            13-> R.drawable.avatar_14
            else -> R.drawable.avatar_1
        })
    }
}

