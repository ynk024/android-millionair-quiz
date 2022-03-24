package de.hsbhv.myquiz.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import de.hsbhv.myquiz.R
import de.hsbhv.myquiz.database.GameDatabase
import de.hsbhv.myquiz.database.GameDatabaseDao
import de.hsbhv.myquiz.databinding.FragmentLeaderBoardBinding


class LeaderBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLeaderBoardBinding>(inflater, R.layout.fragment_leader_board, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDatabaseDao

        val viewModelFactory = LeaderBoardViewModelFactory(dataSource)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(LeaderBoardViewModel::class.java)

        binding.lifecycleOwner = this

        val adapter = LeaderBoardAdapter()
        binding.gamesList.adapter = adapter

        viewModel.games.observe(this, Observer {
            it?.let{
                adapter.data = it
            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigate(LeaderBoardFragmentDirections.actionLeaderBoardFragmentToHomeFragment())
        }


        return binding.root
    }
}