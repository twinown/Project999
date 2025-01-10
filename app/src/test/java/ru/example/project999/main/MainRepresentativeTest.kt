package ru.example.project999.main

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import ru.example.project999.core.UiObserver

class MainRepresentativeTest {

    private lateinit var navigation: FakeNavigation
    private lateinit var representative: MainRepresentative

    @Before
    fun setup() {
        navigation = FakeNavigation.Base()
        representative = MainRepresentative.Base(navigation)
    }

    @Test
    fun test_main() {
        val activityCallback = object : ActivityCallback {
            override fun update(data: Screen) = Unit
        }


        representative.startGettingUpdates(activityCallback)
        navigation.checkUpdateObserverCalled(activityCallback)

        representative.showDashboard(true)
        navigation.checkUpdateCalledCount(1)

        representative.observed()
        navigation.checkClearCalled()

        representative.stopGettingUpdates()
        navigation.checkUpdateObserverCalled(EmptyMainObserver)

        representative.showDashboard(false)
        navigation.checkUpdateCalledCount(1)


    }
}

private interface FakeNavigation : Navigation.Mutable {

    fun checkClearCalled()
    fun checkUpdateObserverCalled(observer: ActivityCallback)
    fun checkUpdateCalledCount(count: Int)
    class Base : FakeNavigation {

        private var clearCalled = false
        private var updateCalledCount = 0
        private var cache: Screen = Screen.Empty

        private var observerCached: UiObserver<Screen> =
            object : ActivityCallback {
                override fun update(data: Screen) = Unit
            }

        override fun checkClearCalled() {
            assertEquals(true, clearCalled)
            clearCalled = false
        }

        override fun checkUpdateObserverCalled(observer: ActivityCallback) {
            assertEquals(observer, observerCached)
        }

        override fun checkUpdateCalledCount(count: Int) {
            assertEquals(count, updateCalledCount)
        }

        override fun clear() {
            clearCalled = true
            cache = Screen.Empty
        }

        override fun update(data: Screen) {
            cache = data
            updateCalledCount++;
        }

        override fun updateObserver(uiObserver: UiObserver<Screen>) {
            observerCached = uiObserver
        }

    }
}

