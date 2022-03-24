package sa.edu.tuwaiq.jaheztask01.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.toRestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "GetRestaurantsUseCase"
class GetRestaurantsUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke(): Flow<State<List<RestaurantItem>>> = flow {
        Log.d(TAG, "inside GetRestaurantsUseCase")
        try {
            emit(State.Loading())
            val restaurants = restaurantRepository.getRestaurantsList().map { it.toRestaurantItem() }
            emit(State.Success(restaurants))
        } catch (e: HttpException) {
            emit(State.Error(e.localizedMessage ?: "unexpected error occurred"))
        } catch (e: IOException) {
            emit(State.Error("couldn't reach server. check your internet connection."))
        }
    }
}