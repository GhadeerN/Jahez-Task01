package sa.edu.tuwaiq.jaheztask01.domain.usecase

import kotlinx.coroutines.flow.Flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import javax.inject.Inject

private const val TAG = "GetRestaurantsUseCase"
class GetRestaurantsUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    suspend operator fun invoke(): Flow<State<List<RestaurantItem>>> = restaurantRepository.getRestaurantsList()
}