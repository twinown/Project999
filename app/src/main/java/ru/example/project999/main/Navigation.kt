package ru.example.project999.main

import ru.example.project999.core.UiObservable
import ru.example.project999.core.UiUpdate
import ru.example.project999.core.UpdateObserver


// TODO: !!!!!!!!!!!!!!!!!!!!!!!???????????
//зачем это?

interface Navigation {

    //переходы между фрагментами/экранами//навигируемся откуда-то//обновляем юай
    //в других экранах только запись
    interface Update : UiUpdate<Screen>

    //
    interface Observe : UpdateObserver<Screen>
    interface Mutable : Update,
        Observe //для мэйн активити. там и тот нужен. и другой, в активити чтение и запись

    class Base : UiObservable.Single<Screen>(), Mutable //наследник сингла - класс,а также

}