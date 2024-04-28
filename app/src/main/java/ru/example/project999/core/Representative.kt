package ru.example.project999.core

interface Representative<T : Any> {

    fun startGettingUpdates(callback: UiObserver<T>) = Unit

    fun stopGettingUpdates() = Unit
}