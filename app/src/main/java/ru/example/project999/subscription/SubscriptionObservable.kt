package ru.example.project999.subscription

import ru.example.project999.core.UiObservable

//SubscriptionObservable : UiObservable<SubscriptionUiState> для удобства
//todo зачем наследоваться интерфейсом от UiObservable, если класс бэйз насл-ся от UiObservable.Single
interface SubscriptionObservable : UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base : UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty),
        SubscriptionObservable {
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
    // TODO: РАЗБЕРИСЬ,КАК РАБОТАЕТ SAVE 
}