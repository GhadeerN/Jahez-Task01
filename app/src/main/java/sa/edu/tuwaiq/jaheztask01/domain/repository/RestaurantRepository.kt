package sa.edu.tuwaiq.jaheztask01.domain.repository

import kotlinx.coroutines.flow.Flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.RestaurantItemDto
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem

interface RestaurantRepository {
    suspend fun getRestaurantsList(): Flow<State<List<RestaurantItem>>>
}