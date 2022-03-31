package sa.edu.tuwaiq.jaheztask01.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.databinding.ProfileFragmentBinding
import sa.edu.tuwaiq.jaheztask01.domain.model.User

private const val TAG = "ProfileFragment"

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        setBaseViewModel(viewModel)
        setUIState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfileInfo()

        // Collect profile UI state
        observeProfileState()
        observeProfileUpdateState()

        onSignOutClicked()

        onNameFieldTextWatcher()
    }

    private fun observeProfileState() {
        lifecycleScope.launch {
            viewModel.profileState.collect { state ->
                if (state.email != null) {
                    setUserInfo(state)
                }
            }
        }
    }

    private fun observeProfileUpdateState() {
        Log.d(TAG, "*** observeProfileUpdateState ***")
        lifecycleScope.launch {
            viewModel.updateProfileState.collect {
                if (it) {
                    Toast.makeText(
                        requireActivity(),
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.updateProfileButton.isEnabled = false
                    binding.updateProfileButton.isClickable = false
                }
            }
        }
    }

    private fun onSignOutClicked() {
        binding.signOutButton.setOnClickListener {
            Log.d(TAG, "signed out!!")
            viewModel.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment).also {
                findNavController().popBackStack(R.id.profileFragment, true)
            }
        }
    }

    private fun setUserInfo(user: User) {
        binding.profileFullName.setText(user.name)
        binding.profileEmail.setText(user.email)
    }

    private fun onNameFieldTextWatcher() {
        val oldName = binding.profileFullName.text.toString()
        binding.outlinedTextFieldName.error = null
        Log.d(TAG, "old name: $oldName")
        binding.profileFullName.doOnTextChanged { text, _, _, count ->
            if (oldName != text.toString()) {
                Log.d(TAG, "update button clickable")
                binding.updateProfileButton.isEnabled = true
                binding.updateProfileButton.isClickable = true
                onUpdatedClicked()
            } else if (count == 0) {
                binding.updateProfileButton.isEnabled = false
                binding.updateProfileButton.isClickable = false
            }
            if (text?.isBlank() == true) {
                binding.updateProfileButton.isEnabled = false
                binding.updateProfileButton.isClickable = false
                binding.outlinedTextFieldName.error = "Name cannot be empty"
            }
        }

    }

    private fun onUpdatedClicked() {
        binding.updateProfileButton.setOnClickListener {
            Log.d(TAG, "onUpdatedClicked")
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateProfile(binding.profileFullName.text.toString().trim())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (binding.profileFullName.text.toString().isBlank()) {
            observeProfileState()
            binding.outlinedTextFieldName.error = null
            binding.updateProfileButton.isEnabled = false
            binding.updateProfileButton.isClickable = false
        }
    }

}