package sa.edu.tuwaiq.jaheztask01.data.repositoryimp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.RestaurantApi
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.toRestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import java.io.IOException
import javax.inject.Inject

private const val TAG = "RestaurantRepositoryImp"

class RestaurantRepositoryImp @Inject constructor(
    private val restaurantApi: RestaurantApi
) : RestaurantRepository {
    override suspend fun getRestaurantsList(): Flow<State<List<RestaurantItem>>> = flow {
        try {
            emit(State.Loading())
            val response = restaurantApi.getRestaurantsList()
            if (response.isSuccessful)
                response.body()?.run {
                    emit(State.Success(this.map { it.toRestaurantItem() }))
                }
            else
                emit(State.Error(response.message()))
        } catch (e: HttpException) {
            emit(State.Error(e.localizedMessage ?: "Unexpected error occurred"))
        } catch (e: IOException) {
            emit(State.Error("Couldn't reach server! Please check your internet connection."))
        }
    }
}