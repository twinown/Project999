package ru.example.project999.main

import androidx.annotation.MainThread
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver

@MainThread
interface MainRepresentative : Representative<Screen> {


    fun showDashboard(firstTime: Boolean)

    class Base(private val navigation: Navigation.Mutable) : MainRepresentative {

        override fun startGettingUpdates(callback: UiObserver<Screen>) {
            navigation.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            //по дефолту там эмпти в контрукторе
            navigation.updateObserver()
        }

        override fun showDashboard(firstTime: Boolean) {
            if (firstTime)
                navigation.update(Screen.Dashboard)
        }
    }
}

