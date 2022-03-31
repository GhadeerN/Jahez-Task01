package sa.edu.tuwaiq.jaheztask01.domain.usecase

import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Boolean = firebaseRepository.isUserLoggedIn()
}