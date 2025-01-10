package ru.example.project999.dashboard

import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import ru.example.project999.core.ClearRepresentative
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.main.Navigation
import ru.example.project999.main.Screen
import ru.example.project999.subscription.presentation.SubscriptionScreen

class DashboardRepresentativeTest {

    private lateinit var representative: DashboardRepresentative
    private lateinit var representativePremium: DashboardRepresentative
    private lateinit var premiumDashboardObservable: FakePremiumObservable
    private lateinit var clear: FakeClear
    private lateinit var navigation: FakeNavigation


    @Before
    fun setup() {
        clear = FakeClear.Base()
        navigation = FakeNavigation.Base()
        premiumDashboardObservable = FakePremiumObservable.Base()
        representative = DashboardRepresentative.Base(
            clear,
            navigation
        )
        representativePremium = DashboardRepresentative.Premium(
            premiumDashboardObservable
        )
    }

    @Test
    fun test_dashBoard_Base() {
        representative.play()
        //actual - тот, что получился по коду
        //а ты передаёшь ожидаемый..то есть говоришь себе : я ожидаю вот это ("это" передаёшь в скобках)
        //  Assert.assertEquals(expected, actual);
        clear.checkClearCalledWith(DashboardRepresentative::class.java)
        navigation.checkUpdated(SubscriptionScreen)
    }

    @Test
    fun test_dashBoard_Premium() {

        val callback = object : DashboardFragment.DashboardObserver { //fake of //Dash// Activity
            override fun update(data: PremiumDashboardUiState) = Unit
        }

        representativePremium.startGettingUpdates(callback)
        premiumDashboardObservable.checkUpdateObserverCalled(callback)

        representativePremium.play()
        premiumDashboardObservable.checkUiState(PremiumDashboardUiState.Playing)

        representativePremium.observed()
        premiumDashboardObservable.checkClearCalled()

        representativePremium.stopGettingUpdates()
        premiumDashboardObservable.checkUpdateObserverCalled(EmptyDashboardObserver)


        representativePremium.startGettingUpdates(callback)
        premiumDashboardObservable.checkUpdateObserverCalled(callback)


    }


}

private interface FakePremiumObservable : PremiumDashboardObservable {

    //    fun checkUpdateCalledCount(time: Int)
    fun checkClearCalled()
    fun checkUiState(uiState: PremiumDashboardUiState)
    fun checkUpdateObserverCalled(observer: DashboardFragment.DashboardObserver)

    class Base : FakePremiumObservable {

        // private var updateCalledCount = 0
        private var clearCalled = false
        private var cache: PremiumDashboardUiState = PremiumDashboardUiState.Empty


        override fun clear() {
            clearCalled = true
            cache = PremiumDashboardUiState.Empty
        }

        override fun update(data: PremiumDashboardUiState) {
            cache = data
            //  updateCalledCount++
        }

        override fun checkClearCalled() {
            assertEquals(true, clearCalled)
            clearCalled = false
        }

        override fun checkUiState(uiState: PremiumDashboardUiState) {
            assertEquals(uiState, cache)
        }

        override fun checkUpdateObserverCalled(observer: DashboardFragment.DashboardObserver) {
            assertEquals(observer, observerCached)
        }

        private var observerCached: UiObserver<PremiumDashboardUiState> =
            object : DashboardFragment.DashboardObserver {
                override fun update(data: PremiumDashboardUiState) = Unit
            }

        override fun updateObserver(uiObserver: UiObserver<PremiumDashboardUiState>) {
            observerCached = uiObserver
        }

    }
}

private interface FakeClear : ClearRepresentative {
    fun checkClearCalledWith(clasz: Class<out Representative<*>>)

    class Base : FakeClear {
        private var clearCalledClasz: Class<out Representative<*>>? = null
        override fun checkClearCalledWith(clasz: Class<out Representative<*>>) {
            assertEquals(clasz, clearCalledClasz)
        }

        override fun clear(clasz: Class<out Representative<*>>) {
            clearCalledClasz = clasz
        }
    }
}

private interface FakeNavigation : Navigation.Update {
    fun checkUpdated(screen: Screen)
    class Base : FakeNavigation {
        private var updateCalledWithScreen: Screen = Screen.Empty
        override fun checkUpdated(screen: Screen) {
            assertEquals(screen, updateCalledWithScreen)
        }

        override fun update(data: Screen) {
            updateCalledWithScreen = data
        }
    }
}