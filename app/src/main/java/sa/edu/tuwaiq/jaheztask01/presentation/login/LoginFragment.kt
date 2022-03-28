package sa.edu.tuwaiq.jaheztask01.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Signup text btn
        binding.signupTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        // Login btn
        binding.loginButton.setOnClickListener {
            Log.d(TAG, "login clicked")
            logIn() // To collect the input fields and call the login functionality
        }

        // Collect the UI state (isLoading, isSuccess, error?)
        lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->

                when {
                    state.isLoading -> {
                        Log.d(TAG, "is loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    state.isSuccess -> {
                        Log.d(TAG, "login success")
                        binding.progressBar.visibility = View.GONE
                        safeNavigate(
                            R.id.loginFragment,
                            R.id.restaurantListFragment
                        )
                    }
                    state.error.isNotBlank() -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), state.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        }
    }

    // This function is to call the login operation
    private fun logIn() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        if (checkLoginValidity(email, password)) {
            Log.d(TAG, "login: $email, $password")
            viewModel.login(email, password)
        }
    }

    // This function is to check the fields validity and show proper error messages to the user
    private fun checkLoginValidity(email: String, password: String): Boolean {
        var state = true

        val emailLayout = binding.outlinedTextFieldEmailLogin
        val passwordLayout = binding.outlinedTextFieldPassLogin

        emailLayout.error = null
        passwordLayout.error = null

        if (email.isBlank()) {
            emailLayout.error = getString(R.string.require)
            state = false
        } else if (!InputFieldValidation.emailsIsValid(email)) {
            emailLayout.error = getString(R.string.wrong_email_format)
            state = false
        }

        if (password.isBlank()) {
            passwordLayout.error = getString(R.string.require)
            state = false
        }

        return state
    }
}

