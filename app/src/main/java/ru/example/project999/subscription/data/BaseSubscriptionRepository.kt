package ru.example.project999.subscription.data

import ru.example.project999.main.UserPremiumCache
import ru.example.project999.subscription.domain.SubscriptionRepository

//репозиторий - это фасад над кэшдатасоурсом и клауддатасорсом
class BaseSubscriptionRepository(
    private val cloudDataSource: SubscriptionCloudDataSource,
    private val userPremiumCache: UserPremiumCache.Save
) : SubscriptionRepository {

    override suspend fun subscribe() {
        //идёшь в сеть, подписываешься
        //потом  кэш записываю флажок , что я стал премиуом
        //вот и корутины, нет коллбэка
        cloudDataSource.subscribe()
        //на клике идёт сохранение в шердпреф
        userPremiumCache.saveUserPremium()
    }
}