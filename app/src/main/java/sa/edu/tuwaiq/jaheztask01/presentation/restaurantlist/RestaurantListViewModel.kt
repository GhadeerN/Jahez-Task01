package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetRestaurantsUseCase
import sa.edu.tuwaiq.jaheztask01.presentation.login.LoginState
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "RestaurantListViewModel"

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase
) : ViewModel() {
    private val _restaurantsState = MutableSharedFlow<RestaurantState>()
    val restaurantsState = _restaurantsState.asSharedFlow()

    init {
        getRestaurantList()
    }

    private fun getRestaurantList() {
        viewModelScope.launch(Dispatchers.IO) {
                getRestaurantsUseCase.invoke().onEach { result ->
                    when(result) {
                        is State.Success -> {
                            _restaurantsState.emit(RestaurantState(restaurants = result.data ?: emptyList()))
                        }
                        is State.Loading -> {
                            _restaurantsState.emit(RestaurantState(isLoading = true))
                        }
                        is State.Error -> {
                            _restaurantsState.emit(RestaurantState(error = result.message ?: "Unexpected error occurred!"))
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }
}