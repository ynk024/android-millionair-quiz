package de.hsbhv.myquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import de.hsbhv.myquiz.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        // get results from checkbox and navigate to GameFragment using navController while using StartFragmentDirections for passing safe-args
        binding.newgameButton.setOnClickListener {
            val isRisiko = binding.risikoSwitch.isChecked
            val hasTimeLimit = binding.timeLimitSwitch.isChecked
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToGameFragment(isRisiko, hasTimeLimit))
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}