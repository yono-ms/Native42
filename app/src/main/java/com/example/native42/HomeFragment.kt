package com.example.native42

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.native42.databinding.HomeFragmentBinding
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.welcomeMessage.collect {
                binding.textView.text = it
            }
        }
        return binding.root
    }

}