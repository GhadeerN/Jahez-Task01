package sa.edu.tuwaiq.jaheztask01.presentation.register

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
import sa.edu.tuwaiq.jaheztask01.common.util.Constants
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_CONFIRM_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_NAME
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.PASSWORDS_DONT_MATCH
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.VALID_INPUTS
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.databinding.SignUpFragmentBinding

private const val TAG = "SignUpFragment"

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {


    private val viewModel: SignUpViewModel by activityViewModels()
    lateinit var binding: SignUpFragmentBinding

    lateinit var name: String
    lateinit var email: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login text btn
        onLogInClicked()

        // Signup btn
        onSignUpClicked()

        // Collect the UI state (isLoading, isSuccess, error?)
        observeSignUpState()
    }

    private fun onLogInClicked() {
        binding.loginTextView.setOnClickListener {
            safeNavigate(R.id.signUpFragment, R.id.loginFragment)
        }
    }

    private fun onSignUpClicked() {
        binding.signupButton.setOnClickListener {
            signup()
        }
    }

    private fun signup() {
        name = binding.registerFullName.text.toString().trim()
        email = binding.registerEmail.text.toString().trim()
        password = binding.registerPassword.text.toString()
        val confirmPassword = binding.registerConfirmPassword.text.toString()

        observeInputState()
        viewModel.inputValidation(name, email, password, confirmPassword)
//        if (checkFields(name, email, password, confirmPassword)) {
//            if (password == confirmPassword) {
//                viewModel.signup(name, email, password)
//            } else
//                binding.outlinedTextFieldRePass.error =
//                    "Confirm password does not match password field"
//        }
    }

    private fun observeInputState() {
        val emailLayout = binding.outlinedTextFieldEmail
        val fullNameLayout = binding.outlinedTextFieldName
        val passwordLayout = binding.outlinedTextFieldPass
        val confirmPassLayout = binding.outlinedTextFieldRePass

        emailLayout.error = null
        fullNameLayout.error = null
        passwordLayout.error = null
        confirmPassLayout.error = null

        lifecycleScope.launch {
            viewModel.inputState.collect { state ->
                state.onEach {
                    when(it) {
                        EMPTY_NAME -> fullNameLayout.error = getString(R.string.require)
                        EMPTY_EMAIL -> emailLayout.error = getString(R.string.require)
                        INVALID_EMAIL -> emailLayout.error = getString(R.string.wrong_email_format)
                        EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.require)
                        EMPTY_CONFIRM_PASSWORD -> confirmPassLayout.error = getString(R.string.require)
                        PASSWORDS_DONT_MATCH -> confirmPassLayout.error = getString(R.string.passwords_dont_match)
                        INVALID_PASSWORD -> passwordLayout.error = getString(R.string.password_format)
                        VALID_INPUTS -> {
                            emailLayout.error = null
                            fullNameLayout.error = null
                            passwordLayout.error = null
                            confirmPassLayout.error = null
                            viewModel.signup(name, email, password)
                        }
                    }
                }
            }
        }
    }

    private fun observeSignUpState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signupState.collectLatest { state ->
                Log.d(TAG, "signUp state: $state")
                when {
                    state.isLoading -> {
                        Log.d(TAG, "is loading")
                        binding.signupProgressBar.visibility = View.VISIBLE
                    }
                    state.isSuccess -> {
                        Log.d(TAG, "signup success")
                        binding.signupProgressBar.visibility = View.GONE
                        safeNavigate(
                            R.id.signUpFragment,
                            R.id.restaurantListFragment
                        ).also {
                            findNavController().popBackStack(R.id.signUpFragment, true)
                        }
                    }
                    state.error.isNotBlank() -> {
                        Log.d(TAG, "signup ERROR ----------------------")
                        binding.signupProgressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), state.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    // This function is to check the require fields validity. Returns true if all fields are valid,
    // and false if not
//    private fun checkFields(
//        fullName: String,
//        email: String,
//        password: String,
//        confirmPassword: String
//    ): Boolean {
//        var state = true
//        val emailLayout = binding.outlinedTextFieldEmail
//        val fullNameLayout = binding.outlinedTextFieldName
//        val passwordLayout = binding.outlinedTextFieldPass
//        val confirmPassLayout = binding.outlinedTextFieldRePass
//
//        emailLayout.error = null
//        fullNameLayout.error = null
//        passwordLayout.error = null
//        confirmPassLayout.error = null
//
//        // Get needed string messages from strings.xml resource
//        val require = getString(R.string.require)
//        val wrongEmailFormat = getString(R.string.wrong_email_format)
//        val passwordConditions = getString(R.string.password_format)
//
//        if (fullName.isBlank()) {
//            fullNameLayout.error = require
//            state = false
//        }
//
//        if (email.isBlank()) {
//            emailLayout.error = require
//            state = false
//        } else if (!InputFieldValidation.emailsIsValid(email)) {
//            emailLayout.error = wrongEmailFormat
//            state = false
//        }
//
//        if (password.isBlank()) {
//            passwordLayout.error = require
//            state = false
//        } else if (!InputFieldValidation.passwordIsValid(password)) {
//            passwordLayout.error = passwordConditions
//            state = false
//        }
//
//        if (confirmPassword.isBlank()) {
//            confirmPassLayout.error = require
//            state = false
//        }
//
//        return state
//    }

}