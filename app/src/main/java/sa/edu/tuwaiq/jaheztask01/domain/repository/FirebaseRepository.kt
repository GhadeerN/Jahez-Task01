package sa.edu.tuwaiq.jaheztask01.domain.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.SharedFlow
import sa.edu.tuwaiq.jaheztask01.common.State

interface FirebaseRepository {
    fun login(email: String, password: String): Task<AuthResult>

}