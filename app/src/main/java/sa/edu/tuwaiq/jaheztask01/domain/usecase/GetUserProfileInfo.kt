package sa.edu.tuwaiq.jaheztask01.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.User
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import java.lang.Exception
import javax.inject.Inject

class GetUserProfileInfo @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<State<User>> = flow {
        try {
            emit(State.Loading())
            val user = firebaseRepository.getUserProfile()
            emit(State.Success(user))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred!"))
        }
    }
}