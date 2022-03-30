package sa.edu.tuwaiq.jaheztask01.presentation.profile

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.common.base.BaseViewModel
import sa.edu.tuwaiq.jaheztask01.domain.model.BaseUIState
import sa.edu.tuwaiq.jaheztask01.domain.model.User
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetUserProfileInfo
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileInfo: GetUserProfileInfo,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel() {
    private val _profileState = MutableStateFlow(User())
    val profileState = _profileState.asStateFlow()

    fun getProfileInfo() {
        getUserProfileInfo.invoke().onEach { result ->

            when (result) {
                is State.Success -> {
                    _profileState.value = result.data!!
                    _baseUIState.emit(BaseUIState())
                }
                is State.Error -> {
                    _baseUIState.emit(
                        BaseUIState(
                            error = result.message ?: "Unexpected error occurred!"
                        )
                    )
                }
                is State.Loading -> {
                    _baseUIState.emit(BaseUIState(isLoading = true))
                }
            }

        }.launchIn(viewModelScope)
    }

    fun signOut() {
        signOutUseCase.invoke()
    }
}