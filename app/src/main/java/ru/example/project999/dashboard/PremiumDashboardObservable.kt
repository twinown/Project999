package ru.example.project999.dashboard

import ru.example.project999.core.UiObservable

//ЧТО ЭТО ТАКОЕ !?для чего
//это маза Оганнеса, тупо для удобства, чтоб работать только с одним словом , а не с двумя типо
// как в навигашне - но тут не нужен мутабл. потому что обновление и изменение происходит в одном месте
//по сути, ты свой "большой" обзервабл переименовываешь в PremiumDashboardObservable
//чтоб в конструктор премиумдэшборд репрезентатива кидать дэшбордобзервабл, а не общий
// TODO: в будущем пойми , почему так - не оч понятно
//видимо, чтоб не переопределять методы UiObservable`а
interface PremiumDashboardObservable : UiObservable<PremiumDashboardUiState> {
    class Base : UiObservable.Single<PremiumDashboardUiState>(PremiumDashboardUiState.Empty),
        PremiumDashboardObservable
}