package sa.edu.tuwaiq.jaheztask01.domain.usecase

import android.util.Log
import com.google.firebase.auth.AuthResult
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
    operator fun invoke(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            Log.d(TAG, "login use case")
            emit(State.Loading())
            firebaseRepository.login(email, password)
            emit(State.Success(true))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred."))
        }
    }
}