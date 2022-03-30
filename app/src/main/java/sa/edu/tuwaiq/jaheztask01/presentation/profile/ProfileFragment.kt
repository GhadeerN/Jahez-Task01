package sa.edu.tuwaiq.jaheztask01.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        _viewModel = viewModel
        setUIState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfileInfo()

        // Collect profile UI state
        observeProfileState()

        onSignOutClicked()
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

}