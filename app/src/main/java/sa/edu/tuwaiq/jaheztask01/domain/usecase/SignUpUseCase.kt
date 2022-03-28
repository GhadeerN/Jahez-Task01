package sa.edu.tuwaiq.jaheztask01.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "SignUpUseCase"
class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(name: String, email: String, password: String): Flow<State<Boolean>> =
        flow {
            try {
                emit(State.Loading())
                firebaseRepository.signup(name, email, password)
                emit(State.Success(true))
            } catch (e: Exception) {
                Log.d(TAG, "SignUpUseCase error ------- ${e.message}")
                emit(State.Error(e.message ?: "Unexpected error occurred."))
            }
        }
}