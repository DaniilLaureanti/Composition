package com.demo.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.composition.R
import com.demo.composition.databinding.FragmentChooseLevelBinding
import com.demo.composition.domain.entity.Level
import java.lang.RuntimeException
import com.demo.composition.presentation.ChooseLevelFragmentDirections as ChooseLevelFragmentDirections1

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnLevelTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
            btnLevelEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnLevelTestNormal.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }
            btnLevelTestHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }

    }

    private fun launchGameFragment(level: Level) {
        findNavController()
            .navigate(
                ChooseLevelFragmentDirections1.actionChooseLevelFragmentToGameFragment(level)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}