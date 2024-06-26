package ru.example.project999.subscription

import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.Core
import ru.example.project999.core.HandleDeath
import ru.example.project999.core.Module
import ru.example.project999.main.UserPremiumCache

class SubscriptionModule(
    private val core: Core,
    private val clear: CleanRepresentative
) : Module<SubscriptionRepresentative> {

    override fun representative() = SubscriptionRepresentative.Base(
        HandleDeath.Base(),
        SubscriptionObservable.Base(),
        clear,
        UserPremiumCache.Base(core.sharedPreferences()),
        //тот же навигашн, что и в остальных - он в одном экзмпляре, хранящемся в аппликашне
        //тот же кор всегда, тот же метод navigation()
        core.navigation()
    )
}