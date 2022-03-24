package sa.edu.tuwaiq.jaheztask01.data.datasource.remote.dto


import com.google.gson.annotations.SerializedName
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem

data class RestaurantItemDto(
    @SerializedName("description")
    val description: String,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("hasOffer")
    val hasOffer: Boolean,
    @SerializedName("hours")
    val hours: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("offer")
    val offer: String
)

// This function is to convert the CoinDto (comes from api) to Coin object (used for UI)
fun RestaurantItemDto.toRestaurantItem(): RestaurantItem {
    return RestaurantItem(
        distance = distance,
        id = id,
        hasOffer = hasOffer,
        hours = hours,
        image = image,
        rating = rating,
        name = name,
        offer = offer
    )
}