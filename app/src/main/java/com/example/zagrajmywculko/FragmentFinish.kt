package com.example.zagrajmywculko

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.zagrajmywculko.databinding.FragmentFinishBinding
import com.example.zagrajmywculko.databinding.FragmentSecondBinding
import com.example.zagrajmywculko.models.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentFinish : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)
        binding.textviewSecond.text = viewModel.points.toString()
        var list = dodajKarty()
        binding.textviewCardsCorrect.text=list[0]
        binding.textviewCardsInCorrect.text=list[1]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonMenu.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFinish_to_FirstFragment)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            getActivity()?.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun dodajKarty():List<String>{
        var cards = viewModel.cards
        var isCorrect = viewModel.cardsCorrect
        var kartyCorrect = ""
        var kartyWrong=""
        for (i in 0 until cards.size){
            if(isCorrect[i]){
                kartyCorrect+=cards[i]+"\n"
            }
            else{
                kartyWrong+=cards[i]+"\n"
            }
        }
        var x= listOf(kartyCorrect,kartyWrong)
        return x

    }

}