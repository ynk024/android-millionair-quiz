package de.hsbhv.myquiz.score

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import de.hsbhv.myquiz.R
import de.hsbhv.myquiz.database.GameDatabase
import de.hsbhv.myquiz.databinding.FragmentScoreBinding
import kotlinx.android.synthetic.main.fragment_score.*

class ScoreFragment : Fragment() {

    private lateinit var scoreFragmentArgs: ScoreFragmentArgs
    private lateinit var avatars: List<View>
    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Data-binding to fragment_score layout
        val binding = DataBindingUtil.inflate<FragmentScoreBinding>(inflater, R.layout.fragment_score, container, false)

        setHasOptionsMenu(true)

        // Retrieving the passed in arguments using the Safe-Args plugin
        scoreFragmentArgs = ScoreFragmentArgs.fromBundle(arguments!!)

        // Setting up the dataSource
        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao

        // Setting up and binding the viewModel to the layout and define this Fragment as lifecycleOwner to observe the LiveData
        val viewModelFactory = ScoreViewModelFactory(dataSource, scoreFragmentArgs.wonCash, scoreFragmentArgs.correctAnswers, scoreFragmentArgs.numQuestions, scoreFragmentArgs.timePlayed, scoreFragmentArgs.hasTimeLimit)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoreViewModel::class.java)
        binding.scoreViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToGame.observe(this, Observer {
            // Resolving crash “IllegalArgumentException x is unknown to this NavController”
            if (findNavController().currentDestination?.id == R.id.scoreFragment) {
                findNavController().navigate(ScoreFragmentDirections.actionRestart(scoreFragmentArgs.isRisiko, scoreFragmentArgs.hasTimeLimit))
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToHome.observe(this, Observer {
            // Resolving crash “IllegalArgumentException x is unknown to this NavController”
            if (findNavController().currentDestination?.id == R.id.scoreFragment) {
                findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToHomeFragment())
                viewModel.doneBackNavigating()
            }
        })

        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.saved_game),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                viewModel.doneShowingSnackbar()
                binding.nicknameEdit.text.clear()
                binding.saveGroup.visibility = View.GONE
                binding.saveResultButton.visibility = View.GONE
            }
        })


        // Set onClickListeners to trigger events in the viewModel
        // Could be set in the layout file onClick attributes since using data-binding
        binding.startButton.setOnClickListener { viewModel.onPlayAgain() }

        binding.saveResultButton.setOnClickListener {
//            viewModel.onSaveGame(binding.nicknameEdit.text.toString())
            binding.saveGroup.visibility = View.VISIBLE
        }

        avatars = listOf(binding.avatarImage1, binding.avatarImage2, binding.avatarImage3, binding.avatarImage4, binding.avatarImage5, binding.avatarImage6, binding.avatarImage7, binding.avatarImage8, binding.avatarImage9, binding.avatarImage10, binding.avatarImage11, binding.avatarImage12, binding.avatarImage13, binding.avatarImage14)
        setAvatarListener()

        binding.backButton.setOnClickListener { viewModel.onBackHome() }

        if(!scoreFragmentArgs.hasTimeLimit)
            binding.saveResultButton.visibility

        return binding.root
    }

    private fun setAvatarListener() {
        for((index, view) in avatars.withIndex()){
            Log.i("ScoreFragment", "$index")
            view.setOnClickListener {
                viewModel.onSaveGame(nickname_edit?.text.toString(), index)
            }
        }
    }

    private fun shareResult() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.share_text, scoreFragmentArgs.correctAnswers, scoreFragmentArgs.numQuestions+1, scoreFragmentArgs.wonCash)
        )
        try {
            startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Teilen ist aktuell nicht verfügbar! Versuche es später nochmal.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_button) {
            shareResult()
        }
        return super.onOptionsItemSelected(item)
    }
}