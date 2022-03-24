package de.hsbhv.myquiz.game

import android.opengl.Visibility
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random.Default.nextInt

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel(private val hasTimeLimit: Boolean, private val isRisiko: Boolean) :
    ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important values in the game, such as game length.
        private const val NUMBER_OF_QUESTIONS = 14

        // This is when the game is over
        private const val DONE = 0L

        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L

        // This is the total time of the game
        private const val COUNTDOWN_TIME = 30000L

    }

    private lateinit var questionList: List<Question>

    /** All the variables needed for the timer **/

    private var timer: CountDownTimer? = null

    val numQuestions = NUMBER_OF_QUESTIONS

    private val _currentTime = MutableLiveData<Long>()

    // The String version of the current time
    val currentTimeString: LiveData<String> = Transformations.map(_currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    /** LiveData - data holder class that can be observed within a given lifecycle **/

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question>
        get() = _currentQuestion

    private val _cash = MutableLiveData<Int>()
    val cash: LiveData<Int>
        get() = _cash

    private val _questionIndex = MutableLiveData<Int>()
    val questionIndex: LiveData<Int>
        get() = _questionIndex

    private val _answerIndex = MutableLiveData<Int>()
    val answerIndex: LiveData<Int>
        get() = _answerIndex

    private val _answerList = MutableLiveData<MutableList<String>>()
    val answerList: LiveData<MutableList<String>>
        get() = _answerList

    private val _rightAnswerIndex = MutableLiveData<Int>()
    val rightAnswerIndex: LiveData<Int>
        get() = _rightAnswerIndex

    private val _timePlayed = MutableLiveData<Long>()
    val timePlayed: LiveData<Long>
        get() = _timePlayed

    val timeJokerButtonVisible = if (hasTimeLimit) View.VISIBLE else View.INVISIBLE


    /** Events for UI observers **/

    // State to decide which answer to color
    private val _stateAnswerIsRight = MutableLiveData<Boolean>()
    val stateAnswerIsRight: LiveData<Boolean>
        get() = _stateAnswerIsRight

    // Event to finish the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // Events for using jokers
    private val _eventPhoneJoker = MutableLiveData<Boolean>()
    val eventPhoneJoker: LiveData<Boolean>
        get() = _eventPhoneJoker


    private val _eventTimeJoker = MutableLiveData<Boolean>()
    val eventAudienceJoker: LiveData<Boolean>
        get() = _eventTimeJoker

    private val _eventFiftyJoker = MutableLiveData<Boolean>()
    val eventFiftyJoker: LiveData<Boolean>
        get() = _eventFiftyJoker


    private val _eventRisikoJoker = MutableLiveData<Boolean>()
    val eventRisikoJoker: LiveData<Boolean>
        get() = _eventRisikoJoker


    // Event that triggers the phone to buzz using different patterns, determined by BuzzType
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    val risikoButtonVisible = if (isRisiko) View.VISIBLE else View.INVISIBLE

    init {
        _cash.value = 0
        _questionIndex.value = 0
        _timePlayed.value = 0L
        questionList = QuestionGenerator.get(NUMBER_OF_QUESTIONS + 1)
        setQuestion()
        setupTimer()
    }

    private fun setQuestion() {
        _currentQuestion.value = questionList[questionIndex.value!!]
        // answers has to be in a mutablelist to call shuffle()
        _answerList.value = _currentQuestion.value!!.answers.toMutableList()
        _answerList.value!!.shuffle()
        // The first answer from the currentQuestion object is always correct
        _rightAnswerIndex.value = _answerList.value!!.indexOf(_currentQuestion.value!!.answers[0])
    }

    private fun setupTimer() {
        // Creates a timer which triggers the end of the game when it finishes
        if (hasTimeLimit) {
            timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

                override fun onTick(millisUntilFinished: Long) {
                    _timePlayed.value = _timePlayed.value?.plus(ONE_SECOND)
                    _currentTime.value = (millisUntilFinished / ONE_SECOND)
                    if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                        _eventBuzz.value =
                            BuzzType.COUNTDOWN_PANIC
                    }
                }

                override fun onFinish() {
                    _currentTime.value =
                        DONE
                    _eventBuzz.value =
                        BuzzType.GAME_OVER
                    _eventGameFinish.value = true
                }
            }
            timer?.start()
        }
    }

    private fun setCash(cashIndex: Int = 0) {
        _cash.value = when (cashIndex) {
            1 -> 50
            2 -> 100
            3 -> 200
            4 -> 300
            5 -> 500
            6 -> 1000
            7 -> 2000
            8 -> 4000
            9 -> 8000
            10 -> 16000
            11 -> 32000
            12 -> 64000
            13 -> 125000
            14 -> 500000
            15 -> 1000000
            else -> 0
        }
    }

    private fun setFinalCash() {
        when (_questionIndex.value) {
            in 0..4 -> setCash(0)
            in 5..9 -> setCash(5)
            in 10..14 -> if (!isRisiko) setCash(10) else setCash(5)
            else -> setCash(15)
        }
    }

    private fun setAnswerIndexOrFinishGame(selectedAnswerIndex: Int) {
        if (selectedAnswerIndex == _rightAnswerIndex.value) {
            _questionIndex.value = (questionIndex.value)?.plus(1)
            setCash(_questionIndex.value!!)
            _stateAnswerIsRight.value = true
            _eventBuzz.value = BuzzType.CORRECT
        } else {
            _stateAnswerIsRight.value = false
            _eventBuzz.value = BuzzType.GAME_OVER
        }
    }

    /** Methods for buttons clicks called from GameFragment **/

    fun onSubmit() : Boolean {
        if (_answerIndex.value != null) {
            val actuallyPlayedQuestions = _questionIndex.value!!
            if (actuallyPlayedQuestions > NUMBER_OF_QUESTIONS || _stateAnswerIsRight.value == false) {
                setFinalCash()
                _eventGameFinish.value = true
            } else {
                _answerIndex.value = null
                setQuestion()
                timer?.start()
            }
            return true
        }
        return false
    }

    fun onSelectAnswer(index: Int = 0) {
        _answerIndex.value = index
        timer?.cancel()
        setAnswerIndexOrFinishGame(index)
    }

    fun onQuitGame() {
        _eventGameFinish.value = true
    }


    fun setRightAnswer() {
        onSelectAnswer(_rightAnswerIndex.value!!)
    }

    fun onInitRisikoJoker() {
        _eventRisikoJoker.value = true
    }

    fun onInitTimeJoker() {
        timer?.cancel()
        _eventTimeJoker.value = true
    }

    fun onInitPhoneJoker() {
        _eventPhoneJoker.value = true
    }

    fun onInitFiftyJoker() {
        _eventFiftyJoker.value = true
    }

    /** Methods for completed events **/

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun onRisikoJokerComplete() {
        _eventRisikoJoker.value = false
    }

    fun onFiftyJokerComplete() {
        _eventFiftyJoker.value = false
    }

    fun onTimeJokerComplete() {
        _eventTimeJoker.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    fun getAnswerFromPhoneCall(): String {
        val possibleAnswers = mutableListOf<Int>()
        // Fill an array length 10 with 7 times the rightAnswerIndex to simulate 70% possibility for the right answer by using the phone joker
        for (x in 0..9) {
            if (x < 4)
                possibleAnswers.add(x)
            else
                possibleAnswers.add(rightAnswerIndex.value!!)
        }
        return answerList.value!![possibleAnswers[(0..9).random()]]
    }
}
