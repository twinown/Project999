package ru.example.project999.dashboard

import ru.example.project999.core.Core
import ru.example.project999.core.Module
import ru.example.project999.main.UserPremiumCache

class DashboardModule(
    private val core: Core
) : Module<DashboardRepresentative> {

    override fun representative(): DashboardRepresentative {
        val cache = UserPremiumCache.Base(core.sharedPreferences())
        return if (cache.isUserPremium()) {
            DashboardRepresentative.Premium(PremiumDashboardObservable.Base())
        } else {
            DashboardRepresentative.Base(core.navigation())
        }
    }
}