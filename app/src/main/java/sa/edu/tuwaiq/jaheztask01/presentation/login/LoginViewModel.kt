package sa.edu.tuwaiq.jaheztask01.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.domain.model.AuthState
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.VALID_INPUTS
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.domain.usecase.IsUserLoggedInUseCase
import sa.edu.tuwaiq.jaheztask01.domain.usecase.LoginUseCase
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel() {
    // Change the UI state via loginState
    private val _loginState = MutableSharedFlow<AuthState>()
    val loginState = _loginState.asSharedFlow()

    // Check the input fields validity
    private val _inputState = MutableSharedFlow<List<Int>>()
    val inputState = _inputState.asSharedFlow()

    suspend fun login(email: String, password: String) {

        Log.d(TAG, "inside login")
        loginUseCase(email, password).onEach { result ->

            when (result) {
                is State.Success -> _loginState.emit(AuthState(isSuccess = true))
                is State.Error -> _loginState.emit(
                    AuthState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                )
                is State.Loading -> _loginState.emit(AuthState(isLoading = true))
            }

        }.launchIn(viewModelScope)
    }

    // This function is to check the fields validity and show proper error messages to the user
    fun inputValidation(email: String, password: String) {
        Log.d(TAG, "inputValidation()")
        viewModelScope.launch {
            var isValid = true
            val inputStates = mutableListOf<Int>()

            if (email.isBlank()) {
                inputStates.add(EMPTY_EMAIL)
                isValid = false
            } else if (!InputFieldValidation.emailsIsValid(email)) {
                inputStates.add(INVALID_EMAIL)
                isValid = false
            }

            if (password.isBlank()) {
                isValid = false
                inputStates.add(EMPTY_PASSWORD)
            }
            _inputState.emit(inputStates)
            Log.d(TAG, "input state list: $inputStates")

            if (isValid)
                _inputState.emit(listOf(VALID_INPUTS))
        }
    }

    // Check if the user is already logged in or not
    fun isUserLoggedIn() = isUserLoggedInUseCase.invoke()

}