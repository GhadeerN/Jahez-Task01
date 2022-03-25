package sa.edu.tuwaiq.jaheztask01.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.AuthState
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _signupState = MutableSharedFlow<AuthState>()
    val signupState = _signupState.asSharedFlow()

    fun signup(name: String, email: String, password: String) {
        signUpUseCase(name, email, password).onEach { result ->
            when (result) {
                is State.Loading -> _signupState.emit(AuthState(isLoading = true))
                is State.Success -> _signupState.emit((AuthState(isSuccess = true)))
                is State.Error -> _signupState.emit(
                    AuthState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                )
            }

        }.launchIn(viewModelScope)
    }
}