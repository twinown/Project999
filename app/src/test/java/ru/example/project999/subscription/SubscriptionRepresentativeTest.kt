package ru.example.project999.subscription

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ru.example.project999.core.ClearRepresentative
import ru.example.project999.core.HandleDeath
import ru.example.project999.core.Representative
import ru.example.project999.core.RunAsync
import ru.example.project999.core.UiObserver
import ru.example.project999.dashboard.DashboardScreen
import ru.example.project999.main.Navigation
import ru.example.project999.main.Screen
import ru.example.project999.subscription.domain.SubscriptionInteractor
import ru.example.project999.subscription.presentation.EmptySubscriptionObserver
import ru.example.project999.subscription.presentation.SaveAndRestoreSubscriptionUiState
import ru.example.project999.subscription.presentation.SubscriptionFragment
import ru.example.project999.subscription.presentation.SubscriptionObservable
import ru.example.project999.subscription.presentation.SubscriptionRepresentative
import ru.example.project999.subscription.presentation.SubscriptionUiState

//логи в тестах не проходят
//тестирование джава/котлин кода ВНЕ андроида
//в юнит-тестах в конструкторы мы принимаем интерфейсы!
//а в функции или примитивы или интерфейсы!
class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative
    private lateinit var observable: FakeObservable
    private lateinit var clear: FakeClear
    private lateinit var interactor: FakeInteractor
    private lateinit var navigation: FakeNavigation
    private lateinit var handleDeath: FakeHandleDeath
    private lateinit var runAsync: FakeRunAsync

    //вызывается перед каждым юнит-тестом
    @Before
    fun setup() {
        observable = FakeObservable.Base()
        clear = FakeClear.Base()
        interactor = FakeInteractor.Base()
        navigation = FakeNavigation.Base()
        handleDeath = FakeHandleDeath.Base()
        runAsync = FakeRunAsync.Base()
        representative = SubscriptionRepresentative.Base(
            runAsync,
            handleDeath,
            observable,
            clear,
            interactor,
            navigation
        )
    }

    //короче, смотри, в репрезентативе, вызывая эту функцию, ты мог передать обычный бандл, который
    //возвращал бы тебе тру или фолс, но при выполнении тестов вот тут ты не можешь закинуть бандл (почему?потому что юнит тесты - это
    // тестирование джава/котлин кода ВНЕ андроида, а бандл - это ос андроид)
    //потому сделали обёртку, которая возвращает нам из обёртки над бандлом просто тру или фолз.
    //restoreState: SaveAndRestoreSubscriptionUiState.Restore - это обертка над бандлом
    //то есть мы можем просто здесь написать фейковые интерфейсы, типо выполняющие роль бандла
    @Test
    fun main_scenario() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback =
            object : SubscriptionFragment.SubscriptionObserver { //fake of //Subscr// Activity
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)

        representative.observed()
        observable.checkClearCalled()

        representative.finish()
        clear.checkClearCalledWith(SubscriptionRepresentative::class.java)
        navigation.checkUpdated(DashboardScreen)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
    }

    @Test
    fun test_save_and_restore() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)

        val callback = object : SubscriptionFragment.SubscriptionObserver { //fake of Activity
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(saveAndRestore)

        representative.init(saveAndRestore)
        //должно быть не больше, чем в первый раз
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUpdateCalledCount(1)

    }

    @Test
    fun test_death_after_loading() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionFragment.SubscriptionObserver { //fake of Activity
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(saveAndRestore)

        //death happening here
        setup()

        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(1)


    }

    @Test
    fun test_death_after_success() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionFragment.SubscriptionObserver { //fake of Activity
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)


        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(saveAndRestore)

        //death happening here
        setup()

        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Success)
        observable.checkUpdateCalledCount(1)
        interactor.checkSubscribeCalledTimes(0)


    }

    @Test
    fun test_death_after_success_observed() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionFragment.SubscriptionObserver { //fake of Activity
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)


        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)
        //я не понял, обзервед всегда же вызывается после смерти, зачем тест -кейс выше
        representative.observed()
        observable.checkClearCalled()
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.save(saveAndRestore)

        //death happening here
        setup()

        representative.init(saveAndRestore)
        handleDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(0)


    }

    @Test
    fun test_cannot_go_back(){
        val callback =
            object : SubscriptionFragment.SubscriptionObserver { //fake of //Subscr// Activity
                override fun update(data: SubscriptionUiState) = Unit
            }
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)
        representative.subscribe()
        representative.comeBack()
        runAsync.checkClearCalledTimes(0)
    }

    @Test
    fun test_can_go_back(){
        val callback =
            object : SubscriptionFragment.SubscriptionObserver { //fake of //Subscr// Activity
                override fun update(data: SubscriptionUiState) = Unit
            }
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)
        representative.subscribe()
        runAsync.pingResult()
        representative.comeBack()
        runAsync.checkClearCalledTimes(1)
    }
}

private interface FakeSaveAndRestore : SaveAndRestoreSubscriptionUiState.Mutable {


    class Base : FakeSaveAndRestore {

        private val state = mutableListOf<SubscriptionUiState>()
        override fun save(data: SubscriptionUiState) {
            state.add(data)
        }

        override fun restore(): SubscriptionUiState {
            return state.last()
        }

        override fun isEmpty(): Boolean {
            return state.isEmpty()
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

private interface FakeInteractor : SubscriptionInteractor {

    fun checkSubscribeCalledTimes(time: Int) {}
    class Base : FakeInteractor {

        private var subscribeCalledCount = 0

        override suspend fun subscribe() {
            subscribeCalledCount++

        }

        override fun checkSubscribeCalledTimes(time: Int) {
            assertEquals(time, subscribeCalledCount)
        }
    }
}

private interface FakeRunAsync:RunAsync{

    fun checkClearCalledTimes(times: Int)
    fun pingResult()
    class Base:FakeRunAsync {

        private var  cachedBlock : (Any) -> Unit = {}
        private var cached : Any = Unit

        override fun pingResult() {
            cachedBlock.invoke(cached)
        }

        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            runBlocking {
              cached = backgroundBlock.invoke()
             cachedBlock = uiBlock as (Any) -> Unit
            }
        }

        private var clearCalledTimes = 0

        override fun checkClearCalledTimes(times: Int) {
            assertEquals(times,clearCalledTimes)
        }
        override fun clear() {
            clearCalledTimes++
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


private interface FakeObservable : SubscriptionObservable {

    fun checkUpdateCalledCount(time: Int)
    fun checkClearCalled()
    fun checkUiState(uiState: SubscriptionUiState)
    fun checkUpdateObserverCalled(observer: SubscriptionFragment.SubscriptionObserver)

    class Base : FakeObservable {
        private var clearCalled = false

        override fun checkClearCalled() {
            assertEquals(true, clearCalled)
            clearCalled = false
        }

        override fun clear() {
            clearCalled = true
            cache = SubscriptionUiState.Empty

        }

        private var updateCalledCount = 0
        private var cache: SubscriptionUiState = SubscriptionUiState.Empty

        override fun update(data: SubscriptionUiState) {
            cache = data
            updateCalledCount++
        }

        override fun checkUpdateCalledCount(time: Int) {
            assertEquals(time, updateCalledCount)
        }

        override fun checkUiState(uiState: SubscriptionUiState) {
            assertEquals(uiState, cache)
        }

        private var observerCached: UiObserver<SubscriptionUiState> =
            object : SubscriptionFragment.SubscriptionObserver {
                override fun update(data: SubscriptionUiState) = Unit

            }

        override fun checkUpdateObserverCalled(observer: SubscriptionFragment.SubscriptionObserver) {
            assertEquals(observer, observerCached)
        }

        override fun updateObserver(uiObserver: UiObserver<SubscriptionUiState>) {
            observerCached = uiObserver
        }

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
}

private interface FakeHandleDeath : HandleDeath {

    fun checkFirstOpeningCalled(times: Int)
    class Base : FakeHandleDeath {

        private var deathHappened = true
        private var firstOpeningCalledTimes = 0

        override fun firstOpening() {
            deathHappened = false
            firstOpeningCalledTimes++
        }

        override fun checkFirstOpeningCalled(times: Int) {
            assertEquals(times, firstOpeningCalledTimes)
        }

        override fun didDeathHappen(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }
}