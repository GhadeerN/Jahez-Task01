package sa.edu.tuwaiq.jaheztask01.domain.usecase

import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke() = firebaseRepository.signOut()
}