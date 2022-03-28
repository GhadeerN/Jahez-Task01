package sa.edu.tuwaiq.jaheztask01.data.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto.RestaurantItemDto

interface RestaurantApi {
    @GET("/restaurants.json")
    suspend fun getRestaurantsList(): Response<List<RestaurantItemDto>>
}