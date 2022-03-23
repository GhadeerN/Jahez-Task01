package sa.edu.tuwaiq.jaheztask01.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
    suspend operator fun invoke(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            Log.d(TAG, "login use case")
            emit(State.Loading())
            val loginResponse = firebaseRepository.login(email, password)
            var isSuccessful = false
            var message: String? = null
            loginResponse.addOnCompleteListener { task ->
                isSuccessful = if (task.isSuccessful) {
                    Log.d(TAG, "login response is successful")
                    true
                } else {
                    Log.d(TAG, "login response failed: ${task.exception?.message}")
                    false
                }
            }.addOnFailureListener {
                Log.d(TAG, "fail listener: ${it.message}")
                message = it.message
            }.await()

            if (isSuccessful) {
                Log.d(TAG, "is successful")
                emit(State.Success(isSuccessful))
            } else {
                Log.d(TAG, "is NOT successful")
                emit(
                    State.Error(
                        message
                            ?: "Login process Failed. Please try again later."
                    )
                )
            }
//

//            if (loginResponse.isSuccessful) {
//                Log.d(TAG, "login response is successful")
//                emit(State.Success(true))
//            } else {
//                Log.d(TAG, "login response fail")
//                emit(
//                    State.Error(
//                        loginResponse.exception?.message
//                            ?: "Login process Failed. Please try again later."
//                    )
//                )
//            }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred."))
        }
    }
}