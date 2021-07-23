package com.example.native42

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.native42.databinding.HomeFragmentBinding
import com.example.native42.databinding.HomeItemBinding
import kotlinx.coroutines.flow.collect

class HomeFragment : BaseFragment() {

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logger.info("onCreateView savedInstanceState=$savedInstanceState")
        binding = HomeFragmentBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.progress.collect {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.welcomeMessage.collect {
                binding.textView.text = it
            }
        }

        binding.button.setOnClickListener {
            logger.debug("button click.")
            viewModel.start()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = HomeAdapter().also { adapter ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.eventMessages.collect { items ->
                    logger.debug("eventMessages=$items")
                    adapter.submitList(items)
                }
            }
        }
        return binding.root
    }

    class HomeAdapter :
        ListAdapter<String, HomeAdapter.ViewHolder>(object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }) {
        class ViewHolder(val binding: HomeItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                HomeItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.textViewMessage.text = getItem(position)
        }
    }

}