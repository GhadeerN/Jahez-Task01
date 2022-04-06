package sa.edu.tuwaiq.jaheztask01.presentation.restaurantlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario.launch
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import sa.edu.tuwaiq.jaheztask01.MainCoroutineRule
import sa.edu.tuwaiq.jaheztask01.common.State
import sa.edu.tuwaiq.jaheztask01.common.base.BaseViewModel
import sa.edu.tuwaiq.jaheztask01.common.util.TestDispatchers
import sa.edu.tuwaiq.jaheztask01.domain.model.RestaurantItem
import sa.edu.tuwaiq.jaheztask01.domain.repository.RestaurantRepository
import sa.edu.tuwaiq.jaheztask01.domain.usecase.GetRestaurantsUseCase

@ExperimentalCoroutinesApi
class RestaurantListViewModelTest {

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var getRestaurantUseCase: GetRestaurantsUseCase
    lateinit var restaurantListViewModel: RestaurantListViewModel
    lateinit var testDispatchers: TestDispatchers
    lateinit var baseViewModel: BaseViewModel

    @Mock
    lateinit var restaurantRepository: RestaurantRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        testDispatchers = TestDispatchers()
        getRestaurantUseCase = GetRestaurantsUseCase(restaurantRepository)
        restaurantListViewModel = RestaurantListViewModel(getRestaurantUseCase, testDispatchers)
        baseViewModel = restaurantListViewModel
    }

    // test if success - with data
    @Test
    fun getRestaurantsData_successWithData() = runBlocking {
        val restaurantList = listOf(
            RestaurantItem(
                id = 1,
                name = "Kudu",
                hours = "05:30 AM - 04:30 AM",
                image = "https://jahez-other-oniiphi8.s3.eu-central-1.amazonaws.com/1.jpg",
                rating = 8.0,
                distance = 0.8866873944226332,
                hasOffer = true,
                offer = "9 SAR"
            )
        )

        Mockito.`when`(restaurantRepository.getRestaurantsList())
            .thenReturn(flow { emit(State.Success(restaurantList)) })

        restaurantListViewModel.getRestaurantList()
        val job = launch {
            restaurantListViewModel.restaurantsState.test {
                val emission = awaitItem()
                assertThat(emission.size).isEqualTo(1)
                cancelAndConsumeRemainingEvents()
            }
        }
        job.join()
        job.cancel()

    }

    // test if success - without data
    @Test
    fun getRestaurantsData_successWithoutData() {
        val restaurantList = listOf<RestaurantItem>()
        runBlocking {
            Mockito.`when`(restaurantRepository.getRestaurantsList())
                .thenReturn(flow { emit(State.Success(restaurantList)) })

            restaurantListViewModel.getRestaurantList()

            val job = launch {
                restaurantListViewModel.restaurantsState.test {
                    val emission = awaitItem()
                    assertThat(emission.size).isEqualTo(0)
                    cancelAndConsumeRemainingEvents()
                }
            }
            job.join()
            job.cancel()
        }
    }

    // test if loading
    @Test
    fun getRestaurantsData_isLoading() {
        runBlocking {
            Mockito.`when`(restaurantRepository.getRestaurantsList())
                .thenReturn(flow { emit(State.Loading()) })

            restaurantListViewModel.getRestaurantList()
            val job = launch {
                baseViewModel.baseUIState.test {
                    val emission = awaitItem()
                    assertThat(emission.isLoading).isTrue()
                    cancelAndConsumeRemainingEvents()
                }
            }
            job.join()
            job.cancel()
        }
    }

    // test if error
    @Test
    fun getRestaurantsData_error() {
        runBlocking {
            Mockito.`when`(restaurantRepository.getRestaurantsList())
                .thenReturn(flow { emit(State.Error("Error!!")) })

            restaurantListViewModel.getRestaurantList()
            val job = launch {
                baseViewModel.baseUIState.test {
                    val emission = awaitItem()
                    assertThat(emission.error).isNotEmpty()
                    cancelAndConsumeRemainingEvents()
                }
            }
            job.join()
            job.cancel()
        }
    }
}