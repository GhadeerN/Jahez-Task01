package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem

data class RestaurantState(
    val isLoading: Boolean = false,
    val restaurants: List<RestaurantItem> = emptyList(),
    val error: String = ""
)
