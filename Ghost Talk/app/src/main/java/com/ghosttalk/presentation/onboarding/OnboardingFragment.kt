package com.ghosttalk.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ghosttalk.R
import com.ghosttalk.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModels()
    private var currentPage = 0

    private val pages = listOf(
        Triple(R.string.onboarding_title_1, R.string.onboarding_desc_1, R.drawable.ic_ghost_1),
        Triple(R.string.onboarding_title_2, R.string.onboarding_desc_2, R.drawable.ic_ghost_3),
        Triple(R.string.onboarding_title_3, R.string.onboarding_desc_3, R.drawable.ic_ghost_5)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePage()
        binding.btnNext.setOnClickListener {
            if (currentPage < pages.lastIndex) {
                currentPage++
                updatePage()
            } else {
                viewModel.completeOnboarding()
                findNavController().navigate(R.id.action_onboarding_to_login)
            }
        }
        binding.btnSkip.setOnClickListener {
            viewModel.completeOnboarding()
            findNavController().navigate(R.id.action_onboarding_to_login)
        }
    }

    private fun updatePage() {
        val (title, desc, icon) = pages[currentPage]
        binding.tvTitle.setText(title)
        binding.tvDescription.setText(desc)
        binding.ivGhost.setImageResource(icon)
        binding.btnNext.text = if (currentPage == pages.lastIndex) {
            getString(R.string.get_started)
        } else {
            getString(R.string.next)
        }
        binding.indicator1.alpha = if (currentPage == 0) 1f else 0.3f
        binding.indicator2.alpha = if (currentPage == 1) 1f else 0.3f
        binding.indicator3.alpha = if (currentPage == 2) 1f else 0.3f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
