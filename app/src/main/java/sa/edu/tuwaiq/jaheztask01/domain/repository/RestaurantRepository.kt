package sa.edu.tuwaiq.jaheztask01.domain.repository

import kotlinx.coroutines.flow.Flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.RestaurantItemDto

interface RestaurantRepository {
    suspend fun getRestaurantsList(): List<RestaurantItemDto>
}