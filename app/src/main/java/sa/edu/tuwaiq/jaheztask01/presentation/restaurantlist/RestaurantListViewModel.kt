package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.common.base.BaseViewModel
import sa.edu.tuwaiq.jaheztask01.common.util.DispatcherProvider
import sa.edu.tuwaiq.jaheztask01.domain.model.BaseUIState
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetRestaurantsUseCase
import javax.inject.Inject

//private const val TAG = "RestaurantListViewModel"

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {
    private val _restaurantsState = MutableStateFlow(listOf<RestaurantItem>())
    val restaurantsState = _restaurantsState.asStateFlow()

    init {
//        getRestaurantList()
    }

     fun getRestaurantList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getRestaurantsUseCase.invoke().onEach { result ->
                when (result) {
                    is State.Success -> {
                        _restaurantsState.value = result.data ?: emptyList()
                        _baseUIState.emit(BaseUIState())
                    }
                    is State.Loading -> {
                        _baseUIState.emit(BaseUIState(isLoading = true))
                    }
                    is State.Error -> {
                        _baseUIState.emit(
                            BaseUIState(
                                error = result.message ?: "Unexpected error occurred!"
                            )
                        )
                    }
                }
            }.flowOn(dispatcherProvider.io).launchIn(viewModelScope)
        }
    }
}