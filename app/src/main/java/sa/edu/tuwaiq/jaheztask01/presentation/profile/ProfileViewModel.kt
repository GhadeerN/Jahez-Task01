package sa.edu.tuwaiq.jaheztask01.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetUserProfileInfo
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignOutUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileInfo: GetUserProfileInfo,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

     fun getProfileInfo() {
        getUserProfileInfo.invoke().onEach { result ->

            when (result) {
                is State.Success -> {
                    _profileState.value = ProfileState(userInfo = result.data)
                }
                is State.Error -> {
                    _profileState.value = ProfileState(
                        error = result.message ?: "Unexpected error occurred!"
                    )
                }
                is State.Loading -> {
                    _profileState.value = ProfileState(isLoading = true)
                }
            }

        }.launchIn(viewModelScope)
    }

    fun signOut() {
        signOutUseCase.invoke()
    }
}