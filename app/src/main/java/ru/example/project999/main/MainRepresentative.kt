package ru.example.project999.main

import androidx.annotation.MainThread
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.dashboard.DashboardScreen

@MainThread
//наследование от маркера для дженерик типизации
interface MainRepresentative : Representative<Screen> {


    fun showDashboard(firstTime: Boolean)

    class Base(private val navigation: Navigation.Mutable) : MainRepresentative {

        override fun startGettingUpdates(callback: UiObserver<Screen>) {

            navigation.updateObserver(callback) //вызывается в uiobservable
        }

        override fun stopGettingUpdates() {
            //по дефолту там эмпти в конструкторе
            navigation.updateObserver()
        }

        override fun showDashboard(firstTime: Boolean) {
            if (firstTime)
            //эта функция ведёт в uiobservable
                navigation.update(DashboardScreen)
        }
    }
}

