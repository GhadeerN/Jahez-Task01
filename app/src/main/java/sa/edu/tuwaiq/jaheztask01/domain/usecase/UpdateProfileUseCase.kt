package sa.edu.tuwaiq.jaheztask01.domain.usecase

import kotlinx.coroutines.flow.Flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(name: String): Flow<State<Boolean>> =
        firebaseRepository.updateProfile(name)
}