package ru.example.project999.subscription

import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.Representative
import ru.example.project999.main.Navigation
import ru.example.project999.main.Screen
import ru.example.project999.main.UserPremiumCache

interface SubscriptionRepresentative : Representative<Unit> {

    fun subscribe()

    class Base(
        private val clear: CleanRepresentative,
        private val userPremiumCache: UserPremiumCache.Save,
        private val navigation: Navigation.Update
    ) : SubscriptionRepresentative {

        override fun subscribe() {
            userPremiumCache.saveUserPremium()
            /*        clear.clear(DashboardRepresentative::class.java)
                    clear.clear(SubscriptionRepresentative::class.java)*/
            navigation.update(Screen.Dashboard)
        }
    }
}