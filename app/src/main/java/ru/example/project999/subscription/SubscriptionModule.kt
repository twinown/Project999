package ru.example.project999.subscription

import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.Core
import ru.example.project999.core.Module
import ru.example.project999.main.UserPremiumCache

class SubscriptionModule(
    private val core: Core,
    private val clear: CleanRepresentative
) : Module<SubscriptionRepresentative> {

    override fun representative() = SubscriptionRepresentative.Base(
        clear,
        UserPremiumCache.Base(core.sharedPreferences()),
        core.navigation()
    )
}