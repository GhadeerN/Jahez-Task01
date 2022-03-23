package sa.edu.tuwaiq.jaheztask01.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _loginState = MutableSharedFlow<LoginState>()
    val loginState = _loginState

    fun login(email: String, password: String) {
        loginUseCase(email, password).onEach { result ->
            when(result) {
                is State.Success -> _loginState.emit(LoginState(isSuccess = true))
                is State.Error -> _loginState.emit(LoginState(error = result.message ?: "Unexpected error occurred!"))
                is State.Loading -> _loginState.emit(LoginState(isLoading = true))
            }
        }.launchIn(viewModelScope)
    }
}