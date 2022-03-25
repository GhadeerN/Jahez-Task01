package sa.edu.tuwaiq.jaheztask01.domain.repository

import com.google.firebase.auth.AuthResult


interface FirebaseRepository {
    suspend fun login(email: String, password: String): AuthResult

    suspend fun signup(name: String, email: String, password: String): AuthResult

    fun isUserLoggedIn(): Boolean

    fun signOut()
}