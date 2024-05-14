package ru.example.project999.dashboard

import ru.example.project999.core.UiObservable

//ЧТО ЭТО ТАКОЕ !?для чего
interface PremiumDashboardObservable : UiObservable<PremiumDashboardUiState> {
    class Base : UiObservable.Single<PremiumDashboardUiState>(), PremiumDashboardObservable
}