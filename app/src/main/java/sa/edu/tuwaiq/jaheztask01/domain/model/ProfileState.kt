package sa.edu.tuwaiq.jaheztask01.domain.model

import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val userInfo: User? = null,
    val error: String = ""
)
