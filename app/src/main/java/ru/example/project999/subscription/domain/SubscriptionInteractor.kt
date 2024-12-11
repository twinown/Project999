package ru.example.project999.subscription.domain

import android.util.Log
import ru.example.project999.main.UserPremiumCache

interface SubscriptionInteractor {

    fun subscribe(callback: () -> Unit)
    class Base(
        private val userPremiumCache: UserPremiumCache.Save
    ) : SubscriptionInteractor {

        override fun subscribe(callback: () -> Unit) {
            Thread {
                Thread.sleep(3000)
                //на клике идёт сохранение в шердпреф
                //   userPremiumCache.saveUserPremium()
                Log.d("nn97", "death can happen here/  before showing success")
                callback.invoke()
            }.start()
        }
    }
}