package sa.edu.tuwaiq.jaheztask01.data.repositoryimp

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharedFlow
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Inject

private const val TAG = "FirebaseRepositoryImp"
class FirebaseRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth
): FirebaseRepository {
    override suspend fun login(email: String, password: String): Task<AuthResult> {
        Log.d(TAG, "Repo imp")
        return auth.signInWithEmailAndPassword(email, password)
    }

}