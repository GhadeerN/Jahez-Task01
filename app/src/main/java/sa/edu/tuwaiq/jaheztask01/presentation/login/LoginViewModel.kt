package sa.edu.tuwaiq.jaheztask01.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.AuthState
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


    fun login(email: String, password: String) {

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

    // Check if the user is already logged in or not
    fun isUserLoggedIn() = isUserLoggedInUseCase.invoke()

}