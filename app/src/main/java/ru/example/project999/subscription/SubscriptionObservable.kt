package ru.example.project999.subscription

import ru.example.project999.core.UiObservable

//SubscriptionObservable : UiObservable<SubscriptionUiState> для удобства
//todo зачем наследоваться интерфейсом от UiObservable, если класс бэйз насл-ся от UiObservable.Single
//todo затем, что ты можешь вызывать методы UiObservabla через SubscriptionObservable, не переопределяя функции сингла
interface SubscriptionObservable : UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base : UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty)
        //после вызова Single здесь в классе UiObservable кэш становится SubscriptionUiState.Empty
        //обсервер становится UiObserver.Empty() то есть это происходит в момент. когда уже вызвался Create фрагмента
        //твоего нового(онкриат в бэйсфрагменте), но потом снова updateObserver и остановится СабскрипшеОбзёрваблом
        //перед этим у тебя кэш - это DashboardScreen
        //обзервер - это Мэйнактивити (речь про первый запуск)
        , SubscriptionObservable {
        //мы сохраняем наш юай стейт всего экрана.но  он находится в обзервабле, поэтому дополняем наш обзёрвабл функцией save
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            //ЧТО ЗА КЭШ ОН СОХРАНЯЕТ??????????скрины /скрин??он сохраняет юайстейт, который в кэше(в uiobservable)
            // , в бандл (сохраняет)
            //cache - это SubscriptionUiState$Initial || $Loading||#Success
            saveState.save(cache)
        }
    }
    // TODO: РАЗБЕРИСЬ,КАК РАБОТАЕТ SAVE #DONE
    //корче, кэш берем из сингла, кидаем сюда.

}
