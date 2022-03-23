package sa.edu.tuwaiq.jaheztask01.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)
