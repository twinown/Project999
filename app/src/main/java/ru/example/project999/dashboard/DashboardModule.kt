package ru.example.project999.dashboard

import ru.example.project999.core.Core
import ru.example.project999.core.Module
import ru.example.project999.main.UserPremiumCache

//бэйз или премиум  решается в модуле, исходя из кэша
//то есть создаём объект дэшбордрепрезентатива: лиюо базовый, либо премиум, смотря на кэш
class DashboardModule(
    private val core: Core
) : Module<DashboardRepresentative> {


    //здесь решается, какой у тебя дэшборд класс -премиум или базовый
    override fun representative(): DashboardRepresentative {
        //получение шерда идёт из провайда
        val cache = UserPremiumCache.Base(core.sharedPreferences())
        return if (cache.isUserPremium()) {
            DashboardRepresentative.Premium(PremiumDashboardObservable.Base())
        } else {
            DashboardRepresentative.Base(core.navigation())
        }
    }
}