package ru.example.project999.dashboard

import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.main.Navigation
import ru.example.project999.main.Screen

interface DashboardRepresentative : Representative<PremiumDashboardUiState> {

    fun play()

    class Base(private val navigation: Navigation.Update) : DashboardRepresentative {
        override fun play() {
            //что вызывает этот метод? апдейт в активити ?
            navigation.update(Screen.Subscription)
        }
    }

    class Premium(private val observable: PremiumDashboardObservable) : DashboardRepresentative {
        override fun play() {
            observable.update(PremiumDashboardUiState.Playing)
        }

        override fun startGettingUpdates(callback: UiObserver<PremiumDashboardUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }
}