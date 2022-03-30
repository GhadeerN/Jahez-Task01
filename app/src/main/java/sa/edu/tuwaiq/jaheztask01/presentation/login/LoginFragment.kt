package sa.edu.tuwaiq.jaheztask01.presentation.login

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
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.VALID_INPUTS
import sa.edu.tuwaiq.jaheztask01.databinding.LoginFragmentBinding

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : BaseFragment() {


    private val viewModel: LoginViewModel by activityViewModels()
    lateinit var binding: LoginFragmentBinding

    lateinit var email: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (viewModel.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_restaurantListFragment)
        }
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        _viewModel = viewModel
        setUIState()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Signup text btn
        onSignUpClicked()

        // Login btn
        onLogInClicked()

        // Collect the UI state (isLoading, isSuccess, error?)
        observeLogInState()
    }

    private fun init() {
        binding.loginEmail.text?.clear()
        binding.loginPassword.text?.clear()
    }

    // onClick events
    private fun onSignUpClicked() {
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun onLogInClicked() {
        binding.loginButton.setOnClickListener {
            Log.d(TAG, "login clicked")
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            observeInputState()
            viewModel.inputValidation(email, password)
        }
    }

    private fun observeInputState() {
        val emailLayout = binding.outlinedTextFieldEmailLogin
        val passwordLayout = binding.outlinedTextFieldPassLogin

        emailLayout.error = null
        passwordLayout.error = null

        Log.d(TAG, "observeInputState()")
        lifecycleScope.launch {
            viewModel.inputState.collect { state ->
                Log.d(TAG, "input state: $state")
                state.onEach {
                    when (it) {
                        EMPTY_EMAIL -> emailLayout.error = getString(R.string.require)
                        INVALID_EMAIL -> emailLayout.error = getString(R.string.wrong_email_format)
                        EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.require)
                        VALID_INPUTS -> {
                            emailLayout.error = null
                            passwordLayout.error = null
                            viewModel.login(email, password)
                        }
                    }
                }
            }
        }
    }

    private fun observeLogInState() {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.loginState) { state ->
            Log.d(TAG, "state: $state")
            if (state) {
                Log.d(TAG, "login success")
                safeNavigate(
                    R.id.loginFragment,
                    R.id.restaurantListFragment
                )
            }

        }
    }

}

