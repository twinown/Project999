package ru.example.project999.dashboard

import ru.example.project999.core.ClearRepresentative
import ru.example.project999.core.Core
import ru.example.project999.core.Module
import ru.example.project999.main.UserPremiumCache

//бэйз или премиум  решается в модуле, исходя из кэша
//то есть создаём объект дэшбордрепрезентатива: лиюо базовый, либо премиум, смотря на кэш
class DashboardModule(
    private val core: Core,
    private val clear: ClearRepresentative
) : Module<DashboardRepresentative> {


    //здесь решается, какой у тебя дэшборд класс -премиум или базовый
    override fun representative(): DashboardRepresentative {
        //получение шерда идёт из провайда
        val cache = UserPremiumCache.Base(core.sharedPreferences())
        return if (cache.isUserPremium()) {
            DashboardRepresentative.Premium(PremiumDashboardObservable.Base())
        } else {
            //тот же навигашн, что и в остальных - он в одном экзмпляре, хранящемся в аппликашне
            //тот же кор всегда, тот же метод navigation()
            DashboardRepresentative.Base(clear, core.navigation()) //Navigation.Base()
        }
    }
}