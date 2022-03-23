package sa.edu.tuwaiq.jaheztask01.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "LoginUseCase"

class LoginUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    // Flow<State<Task<AuthResult>>>
    operator fun invoke(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.Loading())
            val loginResponse = firebaseRepository.login(email, password)
            if (loginResponse.isSuccessful) {
                Log.d(TAG, "login response is successful")
                emit(State.Success(true))
            } else {
                Log.d(TAG, "login response fail")
                emit(
                    State.Error(
                        loginResponse.exception?.message
                            ?: "Login process Failed. Please try again later."
                    )
                )
            }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred."))
        }
//        try {
//            emit(State.Loading())
//            val loginResponse = firebaseRepository.login(email, password)
//            emit(State.Success(loginResponse))
//        } catch (e: IOException) {
//            emit(State.Error("Please check your internet connection."))
//        } catch (e: Exception) {
//            emit(State.Error(e.message ?: "Unexpected error occurred."))
//        }
    }
}