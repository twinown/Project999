package ru.example.project999.subscription.domain

import android.util.Log

interface SubscriptionInteractor {
    // "Interactor" можно перевести как "взаимодействующий компонент" или "компонент для взаимодействия".
//интерактор - это бизнес-логика
    //Interactor — это слой бизнес-логики, который является частью архитектурных паттернов, таких
    // как Clean Architecture. Он представляет собой класс, который обрабатывает бизнес-логику и
    // отвечает за выполнение конкретных операций, связанных с приложением.
    suspend fun subscribe()
    class Base(
        //проксируем = перенаправляем
        private val repository: SubscriptionRepository,
    ) : SubscriptionInteractor {

        override suspend fun subscribe() {
            //  Thread {
            //было
            //Thread.sleep(3000)
            //стало
            repository.subscribe()
                Log.d("nn97", "death can happen here/  before showing success")
            // }.start()
        }
    }
}