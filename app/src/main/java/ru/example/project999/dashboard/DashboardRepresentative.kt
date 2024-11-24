package ru.example.project999.dashboard

import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.main.Navigation
import ru.example.project999.subscription.SubscriptionScreen

interface DashboardRepresentative : Representative<PremiumDashboardUiState> {

    //todo как прога узнаёт , что ты премиум ??
    //дэшборд модуль класс в провайдрепрезентиве(в фабрике при создании объекта дэшбордарепрезентатива)
    // дёргает метод репрезентатив, в этом методе идёт проверка на есть ли в шерде флаг премимум тру
    // иф элс там ..в зависмости от булена создаётся бэйсдэшбордрепрезентативПремиум или Бэйс

    fun play()

    //not premium
    //в констр приходит интерфейс, потому что если бы приходил класс, то было бы нарушение ООП
    //здесь приходит обычный интерфейс, который позволяет тупо переходить с одного фрагмента в другой
    class Base(private val navigation: Navigation.Update) : DashboardRepresentative {

        override fun play() {
            //что вызывает этот метод? апдейт в активити ? да//КАКИМ ОБРАЗОМ ОН ПОНИМАЕТ, ЧТО НАДО ДЕРГАТЬ В АКТИВИТИ!!!???
            //он вызывает метод update() в uiobservable _блок else,там у обзервера снова вызывается метод update()
            //который дёргается в MainActivity


            //ты пойми самое главное- ты не у именно интерфейса вызываешь метод (у инт-са нет экземпляров),а у класса, кто имплементирует этот инт-с
            //например, тут ниже ты вызываешь метод у Navigation.Base()
            //всё завязано на абстракциях

            // TODO: DInversion
            //вообще, всё это D солида, но его реализует DInjection
            //DI - это вот тут, например . у тебя типо у интерфейса вызывается метод, но по факту он вызывается у того
            //кого ты передал в конструктор класса Base
            //а передал ты Navigation.Base(), который имплементирует интерфейс Update:UiUpdate<Screen>,в котором метод update()
            //Navigation.Base() из ProvideSharedPreferences, который дёргается в модуле
            //то же касается и других репрезентативов
            navigation.update(SubscriptionScreen)

        }
    }

    //сюда приходит обзервабл , потому что более функциональный объект быть должен
    //obsevable может обновить вид фрагмента И свяать в startgettingupdates(), чтоб апдекйт вызвался у
    // нужного обзервера - object : UiObserver<PremiumDashboardUiState> в данном случае
    //то есть всё, что ниже делает
    class Premium(private val observable: PremiumDashboardObservable) : DashboardRepresentative {
        override fun play() {
            // TODO: отсюда дебаггером
            observable.update(PremiumDashboardUiState.Playing)
        }

        //когда сюда приходим ?
        //когда ты уже премиум и вызываешь в Dashboard fragment
        // representative.startGettingUpdates(callback)
        //вторая связь, кэш уже нулл, кроме связи
        //из-за того. что они юнит. ты писал их руками
        override fun startGettingUpdates(callback: UiObserver<PremiumDashboardUiState>) {
            observable.updateObserver(callback) //связь фр-та и репрезентатива
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }
}