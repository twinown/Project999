package ru.example.project999.core

//интерфейс маркер/маркеровочный
interface Representative<T : Any> {

    //юнит - для тех репрезентативов и их фрагментов. которым  не нужно обновлять себя!!!!!
    fun startGettingUpdates(callback: UiObserver<T>) = Unit

    fun stopGettingUpdates() = Unit


}