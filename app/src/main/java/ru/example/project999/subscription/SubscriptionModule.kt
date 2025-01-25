package ru.example.project999.subscription

import ru.example.project999.core.ClearRepresentative
import ru.example.project999.core.Core
import ru.example.project999.core.DispatchersList
import ru.example.project999.core.HandleDeath
import ru.example.project999.core.Module
import ru.example.project999.core.RunAsync
import ru.example.project999.main.UserPremiumCache
import ru.example.project999.subscription.data.BaseSubscriptionRepository
import ru.example.project999.subscription.data.SubscriptionCloudDataSource
import ru.example.project999.subscription.domain.SubscriptionInteractor
import ru.example.project999.subscription.presentation.SubscriptionObservable
import ru.example.project999.subscription.presentation.SubscriptionRepresentative

class SubscriptionModule(
    private val core: Core,
    private val clear: ClearRepresentative
) : Module<SubscriptionRepresentative> {

    override fun representative() = SubscriptionRepresentative.Base(
       core.runAsync() ,
        HandleDeath.Base(),
        SubscriptionObservable.Base(),
        clear,
        SubscriptionInteractor.Base(
            BaseSubscriptionRepository(
                SubscriptionCloudDataSource.Base(),
                UserPremiumCache.Base(core.sharedPreferences())
            )
        ),
        //тот же навигашн, что и в остальных - он в одном экзмпляре, хранящемся в аппликашне
        //тот же кор всегда, тот же метод navigation()
        core.navigation()
    )
}