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
import sa.edu.tuwaiq.jaheztask01.databinding.LoginFragmentBinding

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
            logIn()
        }
    }

    private fun logIn() {
        email = binding.loginEmail.text.toString()
        password = binding.loginPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            lifecycleScope.launchWhenStarted {
                viewModel.loginState.collect { state ->
                    when {
                        state.isLoading -> {
                            Log.d(TAG, "is loading")
                        }
                        state.isSuccess -> {
                            Log.d(TAG, "login success")
                        }
                        state.error.isNotBlank() -> {
                            Toast.makeText(requireActivity(), state.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


}

