package com.ghosttalk.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ghosttalk.R
import com.ghosttalk.core.utils.AvatarProvider
import com.ghosttalk.core.utils.NicknameValidator
import com.ghosttalk.databinding.FragmentNicknameBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NicknameFragment : Fragment() {

    private var _binding: FragmentNicknameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var avatarAdapter: AvatarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avatarAdapter = AvatarAdapter(AvatarProvider.allAvatars()) { viewModel.selectAvatar(it) }
        binding.rvAvatars.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = avatarAdapter
        }
        binding.btnContinue.setOnClickListener {
            val nickname = binding.etNickname.text.toString().trim()
            val error = NicknameValidator.getError(nickname)
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(nickname)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    state.error?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show() }
                    if (state.loginSuccess) {
                        findNavController().navigate(R.id.action_nickname_to_home)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedAvatar.collect { avatarAdapter.setSelected(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
