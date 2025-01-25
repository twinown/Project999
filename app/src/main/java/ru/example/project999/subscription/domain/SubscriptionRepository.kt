package ru.example.project999.subscription.domain

//он лежит в domain чтоб интерактор имел к нему доступ
//а в data будет реализация
interface SubscriptionRepository {
    suspend fun subscribe()
}