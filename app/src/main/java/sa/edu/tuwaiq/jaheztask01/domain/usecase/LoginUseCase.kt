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
    suspend operator fun invoke(email: String, password: String): Flow<State<Boolean>> = firebaseRepository.login(email, password)
}