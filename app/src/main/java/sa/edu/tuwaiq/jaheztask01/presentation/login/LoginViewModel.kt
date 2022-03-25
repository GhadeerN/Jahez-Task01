package sa.edu.tuwaiq.jaheztask01.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.usecase.LoginUseCase
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    // Change the UI state via loginState
    private val _loginState = MutableSharedFlow<LoginState>()
    val loginState = _loginState.asSharedFlow()

    fun login(email: String, password: String) {

        Log.d(TAG, "inside login")
        loginUseCase(email, password).onEach { result ->

            when (result) {
                is State.Success -> _loginState.emit(LoginState(isSuccess = true))
                is State.Error -> _loginState.emit(
                    LoginState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                )
                is State.Loading -> _loginState.emit(LoginState(isLoading = true))
            }

        }.launchIn(viewModelScope)
    }
}