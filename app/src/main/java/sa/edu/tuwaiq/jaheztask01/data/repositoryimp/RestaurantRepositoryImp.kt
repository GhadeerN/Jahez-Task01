package sa.edu.tuwaiq.jaheztask01.data.repositoryimp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.RestaurantApi
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.RestaurantItemDto
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import javax.inject.Inject

private const val TAG = "RestaurantRepositoryImp"
class RestaurantRepositoryImp @Inject constructor(
    private val restaurantApi: RestaurantApi
) : RestaurantRepository {
    override suspend fun getRestaurantsList(): List<RestaurantItemDto> {
       return restaurantApi.getRestaurantsList()
    }
}