package sa.edu.tuwaiq.jaheztask01.domain.model

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)
