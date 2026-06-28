package com.ghosttalk.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghosttalk.R
import com.ghosttalk.databinding.FragmentDiscoveryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoveryFragment : Fragment() {

    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiscoveryViewModel by viewModels()
    private lateinit var adapter: UserDiscoveryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserDiscoveryAdapter { user ->
            viewModel.startChat(user.ghostId)
        }
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@DiscoveryFragment.adapter
        }
        binding.etSearch.addTextChangedListener { text ->
            viewModel.search(text?.toString() ?: "")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collect { adapter.submitList(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { binding.progressBar.isVisible = it }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatCreated.collect { chat ->
                    chat?.let {
                        val bundle = androidx.core.os.bundleOf(
                            "chatId" to it.id,
                            "participantName" to it.participant.nickname,
                            "participantId" to it.participant.ghostId
                        )
                        requireActivity().findNavController(com.ghosttalk.R.id.nav_host_fragment)
                            .navigate(com.ghosttalk.R.id.chatDetailFragment, bundle)
                        viewModel.clearChatCreated()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
