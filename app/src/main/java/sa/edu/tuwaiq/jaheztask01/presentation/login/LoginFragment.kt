package sa.edu.tuwaiq.jaheztask01.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.R
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.databinding.LoginFragmentBinding
import javax.inject.Inject

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private val viewModel: LoginViewModel by activityViewModels()
    lateinit var binding: LoginFragmentBinding

    //TODO collect these data from the xml to the viewModel
    lateinit var email: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            Log.d(TAG, "login clicked")
            logIn()
        }
    }

    // This function is to call the login operation
    private fun logIn() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        if (checkLoginValidity(email, password)) {
            Log.d(TAG, "login: $email, $password")
            viewModel.login(email, password)
            lifecycleScope.launch {
                viewModel.loginState.collect { state ->
                    Log.d(TAG, "state: $state")
                    when {
                        state.isLoading -> {
                            Log.d(TAG, "is loading")
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        state.isSuccess -> {
                            Log.d(TAG, "login success")
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_loginFragment_to_restaurantListFragment)
                        }
                        state.error.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireActivity(), state.error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    // This function is to check the fields validity and show proper error messages to the user ----
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

