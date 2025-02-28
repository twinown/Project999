package ru.example.project999.main

import ru.example.project999.core.UiObservable
import ru.example.project999.core.UiUpdate
import ru.example.project999.core.UpdateObserver


// TODO: !!!!!!!!!!!!!!!!!!!!!!!???????????
// почему наследуемся от юаяобсорвабла  ??

interface Navigation {

    //переходы между фрагментами/экранами//навигируемся откуда-то
    //в других экранах только запись
    interface Update : UiUpdate<Screen>
    interface Observe : UpdateObserver<Screen>
    interface Mutable : Update,
        Observe {
        fun clear()
    } //для мэйн активити. там и тот нужен. и другой, в активити чтение и запись
    //запись - имеется ввиду типо updateObserver

    class Base : UiObservable.Base<Screen>(Screen.Empty), Mutable {
    } //наследник сингла - класс,а также мутабла
    //чтоб создать класс Navigation.Base()


}