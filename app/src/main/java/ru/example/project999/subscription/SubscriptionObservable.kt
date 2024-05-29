package ru.example.project999.subscription

import ru.example.project999.core.UiObservable

interface SubscriptionObservable : UiObservable<SubscriptionUiState> {
    class Base : UiObservable.Single<SubscriptionUiState>(), SubscriptionObservable
}