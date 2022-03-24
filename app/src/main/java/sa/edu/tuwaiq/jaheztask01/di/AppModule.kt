package sa.edu.tuwaiq.jaheztask01.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.data.repositoryimp.FirebaseRepositoryImp
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        auth: FirebaseAuth
    ): FirebaseRepository = FirebaseRepositoryImp(auth)
}