package ru.example.project999.subscription.data

import kotlinx.coroutines.delay

//имитация работы некой сети
interface SubscriptionCloudDataSource {
    suspend fun subscribe()
    class Base : SubscriptionCloudDataSource {
        override suspend fun subscribe() {
            delay(5000)
        }
    }


}