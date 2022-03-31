package sa.edu.tuwaiq.jaheztask01.presentation.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.common.base.BaseViewModel
import sa.edu.tuwaiq.jaheztask01.domain.model.BaseUIState
import sa.edu.tuwaiq.jaheztask01.domain.model.User
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetUserProfileInfo
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignOutUseCase
import sa.edu.tuwaiq.jaheztask01.domain.usecase.UpdateProfileUseCase
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileInfo: GetUserProfileInfo,
    private val signOutUseCase: SignOutUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {
    private val _profileState = MutableStateFlow(User())
    val profileState = _profileState.asStateFlow()

    private val _updateProfileState = MutableSharedFlow<Boolean>()
    val updateProfileState = _updateProfileState.asSharedFlow()

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

    suspend fun updateProfile(name: String) {
        updateProfileUseCase(name).onEach { result ->
            Log.d(TAG, "update result: $result")
            when (result) {
                is State.Loading -> _baseUIState.emit(BaseUIState(isLoading = true))
                is State.Error -> _baseUIState.emit(
                    BaseUIState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                )
                is State.Success -> {
                    _updateProfileState.emit(true)
                    _baseUIState.emit(BaseUIState())
                }
            }
        }.launchIn(viewModelScope)
    }
}