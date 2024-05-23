package ru.example.project999.core

import androidx.annotation.MainThread


interface UiObservable<T : Any> : UiUpdate<T>, UpdateObserver<T> {


    //для того, чтоб кэш не держать долго, а выше было (ты удалил). чтоб кэш пинговался
    //отдача данных один раз в ту же активити,короче говоря
    //сингл используется вместе в фризес текст, кэш обнуляется при смерти, а всё живёт за счёт бандла
    abstract class Single<T : Any> : UiObservable<T> {

        @Volatile
        private var cache: T? = null

        @Volatile //чтоб не кэшировалась разными тредами переменная(каждый у себя типо) -
        //в единственном экземпляре
        //эмпти - чтоб не пользоваться нуллами
        private var observer: UiObserver<T> = UiObserver.Empty()


        @MainThread
        //вызывается  обзерваблом ака мэйнрепрезентативом
        //на ONRESUME каждый раз
        override fun updateObserver(uiObserver: UiObserver<T>) = synchronized(lock) {
            observer =
                uiObserver //ключевой момент!!!//апдейт обзервер нужен для связи обзервабла(аппл) и
            //обзервера(активити)//тут и происходит это выше
            if (!observer.isEmpty()) {
                //в ебучем котлине здесь проверка кэша на нулл идёт
                //по сути. сохранение надписи в обсервере ака репрезентативе ака аппликашне
                //вььюха пингуется только
                cache?.let {
                    //эта фигня дёргает метод в мэйн активити
                    //it - это твой кэш , он объект DashboardScreen:Replace
                    observer.update(it)
                    //кэш держим, пока не обновим
                    cache = null
                }
            }
        }

        /**
         * called by  model (типо тред у нас, то есть другой)
         * **/
        override fun update(data: T) = synchronized(lock) {
            //represenative.showdashboard() из активити приходит в мэйнрепрезентатив, там апдейт и приходим сюда,
            // в  if (будет тру)
            if (observer.isEmpty()) {   //еще не вызвался онрезюм у второй активити //поезд ещё не пришёл
                //всегда эмпти
                //но вызвался онпоуз у первой активити
                //этот кэш мб и хранилища какого-то
                cache = data //на онкриэйт акивити каждый раз
                //process death = cache = null
            } else {//а сюда мы приходим, когда в сабскрипшене нажимаешь на кнопку(representative.subscribe()->
                // в сабскррепрезентативе снова , как и выше, navigation.update(), приходящий сюда, но уже в else
                //потому в мэйнактивити(он обсервер) вызываем апдейт
                //тоже апдейт
                cache = null
                //пингуем обзервер ака активити
                observer.update(data)
            }
        }

        //для синхронайзда
        companion object {
            private val lock = Object()
        }
    }
}

//пингование от обзервабла самому обзерверу
//чтоб не писать фунцию в инт-се выше. сделали новый интерфейс
interface UpdateObserver<T : Any> {
    fun updateObserver(uiObserver: UiObserver<T> = UiObserver.Empty())
}

//пингование от обзервера уже активити (типо) через ф -цию
//это наш колбэк активити и его отпрыски типо (фрагменты)
interface UiObserver<T : Any> : UiUpdate<T> {

    fun isEmpty(): Boolean = false

    class Empty<T : Any> : UiObserver<T> {
        override fun isEmpty(): Boolean = true
        override fun update(data: T) = Unit
    }
}

//T точно не null
//этот отдельно для сегрегации, раньше был частью обсервера
interface UiUpdate<T : Any> {
    fun update(data: T)
}
