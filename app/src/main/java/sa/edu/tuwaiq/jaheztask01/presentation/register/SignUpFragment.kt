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
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.common.util.BottomNavBarUtil
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.databinding.SignUpFragmentBinding

private const val TAG = "SignUpFragment"

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {


    private val viewModel: SignUpViewModel by activityViewModels()
    lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide bottom nav bar
        BottomNavBarUtil.get().hidNavBar()

        // Login text btn
        binding.loginTextView.setOnClickListener {
            findNavController().safeNavigate(R.id.signUpFragment, R.id.loginFragment)
        }

        // Signup btn
        binding.signupButton.setOnClickListener {
            signup()
        }

        // Collect the UI state (isLoading, isSuccess, error?)
        lifecycleScope.launch {
            viewModel.signupState.collect { state ->
                when {
                    state.isLoading -> {
                        Log.d(TAG, "is loading")
                        binding.signupProgressBar.visibility = View.VISIBLE
                    }
                    state.isSuccess -> {
                        Log.d(TAG, "signup success")
                        binding.signupProgressBar.visibility = View.GONE
                        findNavController().safeNavigate(
                            R.id.signUpFragment,
                            R.id.restaurantListFragment
                        )
                    }
                    state.error.isNotBlank() -> {
                        Log.d(TAG, "signup ERROR")
                        binding.signupProgressBar.visibility = View.GONE
                        Toast.makeText(requireActivity(), state.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun signup() {
        val name = binding.registerFullName.text.toString().trim()
        val email = binding.registerEmail.text.toString().trim()
        val password = binding.registerPassword.text.toString()
        val confirmPassword = binding.registerConfirmPassword.text.toString()

        if (checkFields(name, email, password, confirmPassword)) {
            if (password == confirmPassword) {
                viewModel.signup(name, email, password)
            } else
                binding.outlinedTextFieldRePass.error =
                    "Confirm password does not match password field"
        }
    }

    // This function is to check the require fields validity. Returns true if all fields are valid,
    // and false if not
    private fun checkFields(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var state = true
        val emailLayout = binding.outlinedTextFieldEmail
        val fullNameLayout = binding.outlinedTextFieldName
        val passwordLayout = binding.outlinedTextFieldPass
        val confirmPassLayout = binding.outlinedTextFieldRePass

        emailLayout.error = null
        fullNameLayout.error = null
        passwordLayout.error = null
        confirmPassLayout.error = null

        // Get needed string messages from strings.xml resource
        val require = getString(R.string.require)
        val wrongEmailFormat = getString(R.string.wrong_email_format)
        val passwordConditions = getString(R.string.password_format)

        if (fullName.isBlank()) {
            fullNameLayout.error = require
            state = false
        }

        if (email.isBlank()) {
            emailLayout.error = require
            state = false
        } else if (!InputFieldValidation.emailsIsValid(email)) {
            emailLayout.error = wrongEmailFormat
            state = false
        }

        if (password.isBlank()) {
            passwordLayout.error = require
            state = false
        } else if (!InputFieldValidation.passwordIsValid(password)) {
            passwordLayout.error = passwordConditions
            state = false
        }

        if (confirmPassword.isBlank()) {
            confirmPassLayout.error = require
            state = false
        }

        return state
    }

}