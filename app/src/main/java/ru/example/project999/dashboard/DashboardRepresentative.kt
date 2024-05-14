package ru.example.project999.dashboard

import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.main.Navigation
import ru.example.project999.subscription.SubscriptionScreen

interface DashboardRepresentative : Representative<PremiumDashboardUiState> {

    //todo как прога узнаёт , что ты премиум ??
    //дэшборд модуль класс в провайдрепрезентиве(а фабрике при создании объекта дэшбордарепрезентатива)
    // дёргает метод репрезентатив, в этом методе идёт проверка на есть ли в шерде флаг премимум тру
    // иф элс там ..в зависмости от булена создаётся бэйсдэшбордрепрезентативПремиум или Бэйсм

    fun play()

    //not premium
    //в констр приходит интерфейс, потому что если бы приходил класс, то было бы нарушение ООП
    //здесь приходит обычный интерфейс, который позволяет тупо переходить с одного фрагмента в другой
    class Base(private val navigation: Navigation.Update) : DashboardRepresentative {
        override fun play() {
            //что вызывает этот метод? апдейт в активити ?
            //он вызывает переход
            navigation.update(SubscriptionScreen)
        }
    }

    //сюда приходит обзервабл , потому что более функциональный объект быть должен
    //то есть всё, что ниже делает
    class Premium(private val observable: PremiumDashboardObservable) : DashboardRepresentative {
        override fun play() {
            observable.update(PremiumDashboardUiState.Playing)
        }

        override fun startGettingUpdates(callback: UiObserver<PremiumDashboardUiState>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }
}