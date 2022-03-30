package sa.edu.tuwaiq.jaheztask01.presentation.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.base.BaseFragment
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_CONFIRM_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_NAME
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.PASSWORDS_DONT_MATCH
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.VALID_INPUTS
import sa.edu.tuwaiq.jaheztask01.databinding.SignUpFragmentBinding

private const val TAG = "SignUpFragment"

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {


    private val viewModel: SignUpViewModel by activityViewModels()
    lateinit var binding: SignUpFragmentBinding

    lateinit var name: TextInputEditText
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpFragmentBinding.inflate(inflater, container, false)
        _viewModel = viewModel
        setUIState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

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
        observeInputState()
        binding.signupButton.setOnClickListener {
            Log.d(TAG, "sign up clicked **")
            val confirmPassword = binding.registerConfirmPassword.text.toString()
            viewModel.inputValidation(
                name.text.toString().trim(),
                email.text.toString().trim(),
                password.text.toString().trim(),
                confirmPassword
            )
        }
    }

    private fun init() {
        name = binding.registerFullName
        email = binding.registerEmail
        password = binding.registerPassword
    }

    private fun observeInputState() {
        Log.d(TAG, "---------- observeInputState --------------")
        val emailLayout = binding.outlinedTextFieldEmail
        val fullNameLayout = binding.outlinedTextFieldName
        val passwordLayout = binding.outlinedTextFieldPass
        val confirmPassLayout = binding.outlinedTextFieldRePass

//        lifecycleScope.launch {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.inputState) { state ->
            emailLayout.error = null
            fullNameLayout.error = null
            passwordLayout.error = null
            confirmPassLayout.error = null
            Log.d(TAG, "state in observe: $state")
            state.forEach {
                when (it) {
                    EMPTY_NAME -> fullNameLayout.error = getString(R.string.require)
                    EMPTY_EMAIL -> emailLayout.error = getString(R.string.require)
                    INVALID_EMAIL -> emailLayout.error = getString(R.string.wrong_email_format)
                    EMPTY_PASSWORD -> passwordLayout.error = getString(R.string.require)
                    EMPTY_CONFIRM_PASSWORD -> confirmPassLayout.error =
                        getString(R.string.require)
                    PASSWORDS_DONT_MATCH -> confirmPassLayout.error =
                        getString(R.string.passwords_dont_match)
                    INVALID_PASSWORD -> passwordLayout.error =
                        getString(R.string.password_format)
                    VALID_INPUTS -> {
                        Log.d(TAG, "----------- valid fields ------------")
                        emailLayout.error = null
                        fullNameLayout.error = null
                        passwordLayout.error = null
                        confirmPassLayout.error = null
                        viewModel.signup(
                            name.text.toString().trim(),
                            email.text.toString().trim(),
                            password.text.toString().trim()
                        )
                    }
                }
            }
        }
    }

    private fun observeSignUpState() {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.signupState) { state ->
            Log.d(TAG, "signUp state: $state")
            if (state) {
                Log.d(TAG, "signup success -----")
                findNavController().navigate(R.id.action_signUpFragment_to_restaurantListFragment)
                    .also {
                        findNavController().popBackStack(R.id.signUpFragment, true)
                    }
            }
        }
    }

}