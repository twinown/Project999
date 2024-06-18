package ru.example.project999.subscription

import ru.example.project999.core.UiObservable


interface SubscriptionObservable : UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base : UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty),
        SubscriptionObservable {
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
    // TODO: РАЗБЕРИСЬ,КАК РАБОТАЕТ SAVE 
}