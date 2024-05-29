package ru.example.project999.subscription

import android.util.Log
import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.HandleDeath
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.dashboard.DashboardRepresentative
import ru.example.project999.dashboard.DashboardScreen
import ru.example.project999.main.Navigation
import ru.example.project999.main.UserPremiumCache

interface SubscriptionRepresentative : Representative<SubscriptionUiState> {

    fun init(firstOpening: Boolean)
    fun subscribe()
    fun finish()

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val clear: CleanRepresentative,
        private val userPremiumCache: UserPremiumCache.Save,
        private val navigation: Navigation.Update
    ) : SubscriptionRepresentative {

        //init блок = конструктор
        //первичный конструктор уже есть
        init {
            Log.d("nn97", "SubscriptionRepresentative init")
        }

        override fun init(firstOpening: Boolean) {
            //топ хэндлер смерти процесса
            if (firstOpening) {
                observable.update(SubscriptionUiState.Initial)
                //init local cache
                //       localCache = "a"
                Log.d("nn97", "this is very first opening the app")
                handleDeath.firstOpening()
            } else {
                if (handleDeath.wasDeathHappened()) {
                    //go to permanent storage and get localCache
                    Log.d("nn97", "death happened")
                    handleDeath.deathHandled()
                }/* else {
                    //use local cache and dont use permanent
                    Log.d("nn97", "just activity recreated")
                }*/
            }
        }

        override fun subscribe() {
            Thread {
                observable.update(SubscriptionUiState.Loading)
                //на клике идёт сохранение в шердпреф
                userPremiumCache.saveUserPremium()
                clear.clear(DashboardRepresentative::class.java)
                clear.clear(SubscriptionRepresentative::class.java)
                observable.update(SubscriptionUiState.Success)
            }.start()
        }

        override fun finish() {
            //переход на дэшборд. дёргается в мэйнактивити, видишь. тут навигашн
            navigation.update(DashboardScreen)
        }

        override fun startGettingUpdates(callback: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()

        }
    }
}