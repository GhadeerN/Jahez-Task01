package sa.edu.tuwaiq.jaheztask01.common.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchers: DispatcherProvider {
    val testDispatcher = TestCoroutineDispatcher()
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
}