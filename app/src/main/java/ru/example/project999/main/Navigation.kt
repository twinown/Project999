package ru.example.project999.main

import ru.example.project999.core.UiObservable
import ru.example.project999.core.UiUpdate
import ru.example.project999.core.UpdateObserver

interface Navigation {

    interface Update : UiUpdate<Screen>
    interface Observe : UpdateObserver<Screen>
    interface Mutable : Update, Observe

    class Base : UiObservable.Single<Screen>(), Mutable

}