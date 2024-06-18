package ru.example.project999.core

//интерфейс маркер/маркеровочный
interface Representative<T : Any> {

    //юнит - для тех репрезентативов и их фрагментов. которым  не нужно обновлять себя!!!!!
    //метод так называется , потому что мы начинаем получать обновления для нашего фрагмента
    fun startGettingUpdates(callback: UiObserver<T>) = Unit

    fun stopGettingUpdates() = Unit


}