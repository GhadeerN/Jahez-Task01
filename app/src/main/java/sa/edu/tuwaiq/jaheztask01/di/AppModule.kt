package sa.edu.tuwaiq.jaheztask01.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sa.edu.tuwaiq.jaheztask01.common.util.Constants
import sa.edu.tuwaiq.jaheztask01.common.util.DefaultDispatchers
import sa.edu.tuwaiq.jaheztask01.common.util.DispatcherProvider
import sa.edu.tuwaiq.jaheztask01.common.util.InputFieldValidation
import sa.edu.tuwaiq.jaheztask01.data.datasource.remote.RestaurantApi
import sa.edu.tuwaiq.jaheztask01.data.repositoryimp.FirebaseRepositoryImp
import sa.edu.tuwaiq.jaheztask01.data.repositoryimp.RestaurantRepositoryImp
import sa.edu.tuwaiq.jaheztask01.domain.repository.FirebaseRepository
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    //Authorization
    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        auth: FirebaseAuth
    ): FirebaseRepository = FirebaseRepositoryImp(auth)

    //API
    @Singleton
    @Provides
    fun provideRestaurantApi(): RestaurantApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(RestaurantApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRestaurantRepository(
        restaurantApi: RestaurantApi
    ): RestaurantRepository {
        return RestaurantRepositoryImp(restaurantApi)
    }

    // Dispatcher provider
    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatchers()
}