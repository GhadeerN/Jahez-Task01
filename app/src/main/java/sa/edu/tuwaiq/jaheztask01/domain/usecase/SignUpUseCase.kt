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
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<State<Boolean>> = firebaseRepository.signup(name, email, password)
}