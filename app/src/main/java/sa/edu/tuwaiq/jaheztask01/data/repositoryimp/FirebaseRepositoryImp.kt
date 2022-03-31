package sa.edu.tuwaiq.jaheztask01.data.repositoryimp

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.model.User
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "FirebaseRepositoryImp"

class FirebaseRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseRepository {
    override suspend fun login(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            var success = false
            emit(State.Loading())
            Log.d(TAG, "Repo imp")
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                success = task.isSuccessful
            }.await()
            if (success)
                emit(State.Success(success))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred."))
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Flow<State<Boolean>> = flow {
        try {
            emit(State.Loading())
            var success = false
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    success = true
                    auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        Log.d(TAG, "display name: ${auth.currentUser?.displayName}")
                    }
                }
            }.await()
            Log.d(TAG, "repo imp: success - $success,")
            if (success)
                emit(State.Success(success))
        } catch (e: Exception) {
            Log.d(TAG, "catch: ${e.message}")
            emit(State.Error(e.message ?: "Unexpected error occurred."))
        }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun signOut() {
        auth.signOut()
    }

    override fun getUserProfile(): Flow<State<User>> = flow {
        try {
            emit(State.Loading())
            val user = User(
                auth.currentUser!!.displayName ?: "",
                auth.currentUser!!.email!!
            )
            emit(State.Success(user))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred!"))
        }
    }

    override suspend fun updateProfile(name: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.Loading())
            var success = false
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                        success = true
                    }
                }.await()
            if (success)
                emit(State.Success(success))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unexpected error occurred!"))
        }
    }

}