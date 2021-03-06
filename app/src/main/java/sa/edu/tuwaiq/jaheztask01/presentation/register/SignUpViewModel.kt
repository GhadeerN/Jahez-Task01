package sa.edu.tuwaiq.jaheztask01.presentation.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.common.base.BaseViewModel
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_CONFIRM_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_NAME
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.EMPTY_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_EMAIL
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.INVALID_PASSWORD
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.PASSWORDS_DONT_MATCH
import sa.edu.tuwaiq.jaheztask01.common.util.Constants.VALID_INPUTS
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.domain.model.BaseUIState
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignUpUseCase
import javax.inject.Inject

private const val TAG = "SignUpViewModel"

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : BaseViewModel() {
    private val _signupState = MutableSharedFlow<Boolean>()
    val signupState = _signupState.asSharedFlow()

    // Check the input fields validity
    private val _inputState = MutableSharedFlow<List<Int>>()
    val inputState = _inputState.asSharedFlow()

    suspend fun signup(name: String, email: String, password: String) {
        signUpUseCase(name, email, password).onEach { result ->
            when (result) {
                is State.Loading -> _baseUIState.emit(BaseUIState(isLoading = true))
                is State.Success -> {
                    _signupState.emit(true)
                    _baseUIState.emit(BaseUIState())
                }
                is State.Error -> _baseUIState.emit(
                    BaseUIState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                )
            }

        }.launchIn(viewModelScope)
    }

    fun inputValidation(name: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            var isValid = true
            val inputStates = mutableListOf<Int>()

            if (name.isBlank()) {
                inputStates.add(EMPTY_NAME)
                isValid = false
            }

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
            } else if (!InputFieldValidation.passwordIsValid(password)) {
                isValid = false
                inputStates.add(INVALID_PASSWORD)
            }

            if (confirmPassword.isBlank()) {
                isValid = false
                inputStates.add(EMPTY_CONFIRM_PASSWORD)
            } else if (password != confirmPassword) {
                isValid = false
                inputStates.add(PASSWORDS_DONT_MATCH)
            }

            if (isValid) {
                inputStates.clear()
                inputStates.add(VALID_INPUTS)
            }
            Log.d(TAG, "ViewModel ---- input state list: $inputStates")
            _inputState.emit(inputStates)
        }
    }
}