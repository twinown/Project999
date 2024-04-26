package ru.example.project999.dashboard

import ru.example.project999.core.UiObservable

interface PremiumDashboardObservable : UiObservable<PremiumDashboardUiState> {
    class Base : UiObservable.Single<PremiumDashboardUiState>(), PremiumDashboardObservable
}