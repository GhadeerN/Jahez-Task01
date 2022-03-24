package sa.edu.tuwaiq.jaheztask01.domain.model

import com.google.gson.annotations.SerializedName

data class RestaurantItem(
    val distance: Double,
    val hasOffer: Boolean,
    val hours: String,
    val image: String,
    val name: String,
    val rating: Double,
    val id: Int,
    val offer: String
)
