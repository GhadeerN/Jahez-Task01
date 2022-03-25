package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetRestaurantsUseCase
import sa.edu.tuwaiq.jaheztask01.domain.usecase.SignOutUseCase
import sa.edu.tuwaiq.jaheztask01.presentation.login.LoginState
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "RestaurantListViewModel"

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    //TODO for test
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _restaurantsState = MutableStateFlow(RestaurantState())
    val restaurantsState = _restaurantsState.asStateFlow()

    init {
        getRestaurantList()
    }

    private fun getRestaurantList() {
        viewModelScope.launch(Dispatchers.IO) {
                getRestaurantsUseCase.invoke().onEach { result ->
                    when(result) {
                        is State.Success -> {
                            _restaurantsState.value = RestaurantState(restaurants = result.data ?: emptyList())
                        }
                        is State.Loading -> {
                            _restaurantsState.value = RestaurantState(isLoading = true)
                        }
                        is State.Error -> {
                            _restaurantsState.value = RestaurantState(error = result.message ?: "Unexpected error occurred!")
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun signOut() {
        signOutUseCase.invoke()
    }
}