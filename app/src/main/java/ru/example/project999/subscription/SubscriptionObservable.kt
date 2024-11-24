package ru.example.project999.subscription

import ru.example.project999.core.UiObservable

//SubscriptionObservable : UiObservable<SubscriptionUiState> для удобства
//todo зачем наследоваться интерфейсом от UiObservable, если класс бэйз насл-ся от UiObservable.Single
interface SubscriptionObservable : UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base : UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty),
        //после вызова Single здесь в классе UiObservable кэш становится SubscriptionUiState.Empty
        //обсервер становится UiObserver.Empty() то есть это происходит в момент. когда уже вызвался Create фрагмента
        //твоего нового(онкриат в бэйсфрагменте), но потом снова updateObserver и остановится СабскрипшеОбзёрваблом
        //перед этим у тебя кэш - это DashboardScreen
        //обзервер - это Мэйнактивити (речь про первый запуск)
        SubscriptionObservable {
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
    // TODO: РАЗБЕРИСЬ,КАК РАБОТАЕТ SAVE 
}