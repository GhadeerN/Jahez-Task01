package sa.edu.tuwaiq.jaheztask01.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import sa.edu.tuwaiq.jaheztask01.domain.model.User


interface FirebaseRepository {
    suspend fun login(email: String, password: String): AuthResult

    suspend fun signup(name: String, email: String, password: String): AuthResult

    fun isUserLoggedIn(): Boolean

    fun signOut()

    fun getUserProfile(): User
}