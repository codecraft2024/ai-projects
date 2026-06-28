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
import com.ghosttalk.R
import com.ghosttalk.core.utils.PhoneValidator
import com.ghosttalk.databinding.FragmentMobileLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MobileLoginFragment : Fragment() {

    private var _binding: FragmentMobileLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MobileLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMobileLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            if (!PhoneValidator.isValid(phone)) {
                Snackbar.make(binding.root, R.string.invalid_phone, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.sendOtp(phone)
        }
        binding.btnVerify.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            val otp = binding.etOtp.text.toString().trim()
            viewModel.verifyOtp(phone, otp)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    binding.tilOtp.isVisible = state.otpSent
                    binding.btnVerify.isVisible = state.otpSent
                    binding.btnSendOtp.isVisible = !state.otpSent
                    state.error?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                    if (state.loginSuccess) {
                        findNavController().navigate(R.id.action_mobile_to_home)
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
