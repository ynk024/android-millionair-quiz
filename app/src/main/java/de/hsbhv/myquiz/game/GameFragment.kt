package de.hsbhv.myquiz.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import de.hsbhv.myquiz.R
import de.hsbhv.myquiz.databinding.FragmentGameBinding

class GameFragment : Fragment() {


    private lateinit var answerViews: List<View>
    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var gameFragmentArgs: GameFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        gameFragmentArgs = GameFragmentArgs.fromBundle(arguments!!)
        answerViews = listOf(
            binding.answerAText,
            binding.answerBText,
            binding.answerCText,
            binding.answerDText
        )
        setupViewModel()
        binding.lifecycleOwner = this


        viewModel.stateAnswerIsRight.observe(this, Observer { isAnswerRight ->
            setAnswerListeners(isClickable = false)
            colorAnswerViews(isAnswerRight)
        })

        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(this, Observer { isFinished ->
            finishGame(isFinished)
        })

        viewModel.eventFiftyJoker.observe(this, Observer {
            disableTwoOptions()
        })

        viewModel.eventRisikoJoker.observe(this, Observer {
            viewModel.setRightAnswer()
        })

        viewModel.eventPhoneJoker.observe(this, Observer {
            Toast.makeText(
                context,
                "Ich bin ziemlich sicher die richtige Antwort ist: ${viewModel.getAnswerFromPhoneCall()}",
                Toast.LENGTH_LONG
            ).show()
        })


        // set listeners for buttons
        binding.submitButton.setOnClickListener {
            if (viewModel.onSubmit()) {
                setAnswerListeners(isClickable = true)
                resetColors()
            }

        }

        binding.risikoJokerButton.setOnClickListener {
            viewModel.onInitRisikoJoker()
            it.apply {
                isEnabled = false
                setBackgroundResource(0)
            }
        }

        binding.timeJokerButton.setOnClickListener {
            viewModel.onInitTimeJoker()
            Toast.makeText(
                context,
                "Die Zeit ist jetzt für diese Runde gestoppt.",
                Toast.LENGTH_LONG
            ).show()
            it.apply {
                isEnabled = false
                setBackgroundResource(0)
            }
        }

        binding.fiftyJokerButton.setOnClickListener {
            viewModel.onInitFiftyJoker()
            it.apply {
                isEnabled = false
                setBackgroundResource(0)
            }
        }

        binding.callJokerButton.setOnClickListener {
            viewModel.onInitPhoneJoker()
            it.apply {
                isEnabled = false
                setBackgroundResource(0)
            }
        }

        viewModel.eventBuzz.observe(this, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

        // initialize answerViews and set listeners


        setAnswerListeners(true)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun finishGame(isFinished: Boolean) {
        if (isFinished) {
            val cash = viewModel.cash.value ?: 0
            val correctAnswers = viewModel.questionIndex.value ?: 0
            val timePlayed = viewModel.timePlayed.value ?: 0
            val action = GameFragmentDirections.actionGameFragmentToScoreFragment(
                cash, gameFragmentArgs.isRisiko, gameFragmentArgs.hasTimeLimit, correctAnswers,
                viewModel.numQuestions, timePlayed
            )
            findNavController(this).navigate(action)
            viewModel.onGameFinishComplete()
        }
    }

    private fun colorAnswerViews(isAnswerRight: Boolean) {
        val chosenAnswerIndex = viewModel.answerIndex.value!!
        val rightAnswerIndex = viewModel.rightAnswerIndex.value ?: 0
        answerViews[rightAnswerIndex].setBackgroundResource(R.color.answer_right)
        if (!isAnswerRight) {
            answerViews[chosenAnswerIndex].setBackgroundResource(R.color.answer_wrong)
        }
    }

    private fun disableTwoOptions() {
        val rightAnswerIndex = viewModel.rightAnswerIndex.value!!
        val secondOption = when (rightAnswerIndex) {
            3 -> 0
            else -> rightAnswerIndex + 1
        }
        for ((i, view) in answerViews.withIndex()) {
            if (i == rightAnswerIndex || i == secondOption) {
                view.setOnClickListener {
                    viewModel.onSelectAnswer(i)
                }
            } else {
                answerViews[i].setBackgroundResource(android.R.color.darker_gray)
                view.setOnClickListener {

                }
            }
        }

    }

    private fun setupViewModel() {
        viewModelFactory =
            GameViewModelFactory(gameFragmentArgs.hasTimeLimit, gameFragmentArgs.isRisiko)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel
    }

    private fun setAnswerListeners(isClickable: Boolean) {
        for ((i, view) in answerViews.withIndex()) {
            view.setOnClickListener {
                if (isClickable) {
                    viewModel.onSelectAnswer(i)
                }
            }
        }
    }

    private fun resetColors() {
        for (view in answerViews)
            view.setBackgroundResource(R.color.colorQuestion)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}

