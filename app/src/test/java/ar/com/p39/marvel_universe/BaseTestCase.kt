package ar.com.p39.marvel_universe

import ar.com.p39.marvel_universe.utils.CoroutineDispatcherRule
import ar.com.p39.marvel_universe.utils.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseTestCase {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineDispatcherRule()

    @Before
    fun openMocks() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}