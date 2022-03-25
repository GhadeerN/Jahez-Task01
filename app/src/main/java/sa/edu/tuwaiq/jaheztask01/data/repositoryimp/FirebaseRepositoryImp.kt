package sa.edu.tuwaiq.jaheztask01.data.repositoryimp

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Inject

private const val TAG = "FirebaseRepositoryImp"

class FirebaseRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseRepository {
    override suspend fun login(email: String, password: String): AuthResult {
        Log.d(TAG, "Repo imp")
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signup(name: String, email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await().also {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            auth.currentUser?.updateProfile(profileUpdates)?.await()
            Log.d(TAG, "display name: ${auth.currentUser?.displayName}")
        }
    }

}