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
            // эта функция ведёт в uiobservable
            //там дёргается метод observer.update(), тк кэш не нулл, который стреляет в MainActivity
            navigation.updateObserver(callback) //вызывается в uiobservable
        }

        override fun stopGettingUpdates() {
            //по дефолту там эмпти в конструкторе
            navigation.updateObserver()
        }

        override fun showDashboard(firstTime: Boolean) {
            if (firstTime) //ПЕРВЫЙ СТАРТ ПРИЛОЖЕНИЯ
            //эта функция выз-ся в mainactivity, по итогу
            // отсюда идёт в uiobservable, там и дёргается..observer.isEmpty()==true
            //сохранение DashboardScreen : Screen.Replace(DashboardFragment::class.java) в кэш
            // ведь твой мэйн репрезентатив - обзервабл
                navigation.update(DashboardScreen)
        }
    }
}

