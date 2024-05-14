package ru.example.project999.subscription

import android.util.Log
import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.Representative
import ru.example.project999.dashboard.DashboardRepresentative
import ru.example.project999.dashboard.DashboardScreen
import ru.example.project999.main.Navigation
import ru.example.project999.main.UserPremiumCache

interface SubscriptionRepresentative : Representative<Unit> {

    fun subscribe()

    class Base(
        private val clear: CleanRepresentative,
        private val userPremiumCache: UserPremiumCache.Save,
        private val navigation: Navigation.Update
    ) : SubscriptionRepresentative {

        //init блок = конструктор
        //первичный конструктор уже есть
        init {
            Log.d("nn97", "SubscriptionRepresentative init")
        }

        override fun subscribe() {
            //на клике идёт сохранение в шердпреф
            userPremiumCache.saveUserPremium()
            clear.clear(DashboardRepresentative::class.java)
            clear.clear(SubscriptionRepresentative::class.java)
            //переход на дэшборд
            navigation.update(DashboardScreen)
        }
    }
}