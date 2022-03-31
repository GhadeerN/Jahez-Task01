package sa.edu.tuwaiq.jaheztask01.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.User


interface FirebaseRepository {
    suspend fun login(email: String, password: String): Flow<State<Boolean>>

    suspend fun signup(name: String, email: String, password: String): Flow<State<Boolean>>

    fun isUserLoggedIn(): Boolean

    fun signOut()

    fun getUserProfile(): Flow<State<User>>

    suspend fun updateProfile(name: String): Flow<State<Boolean>>
}