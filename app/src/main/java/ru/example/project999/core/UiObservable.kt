package ru.example.project999.core

import androidx.annotation.MainThread


interface UiObservable<T : Any> : UiUpdate<T>, UpdateObserver<T> {

    fun clear()

    //для того, чтоб кэш не держать долго, а выше было (ты удалил). чтоб кэш пинговался
    //отдача данных один раз в ту же активити,короче говоря
    //сингл используется вместе в фризес текст, кэш обнуляется при смерти, а всё живёт за счёт бандла
    abstract class Single<T : Any>(
        private val empty: T
    ) : UiObservable<T> {


        @Volatile
        //был private
        internal var cache: T = empty

        @Volatile //чтоб не кэшировалась разными тредами переменная(каждый у себя типо) -
        //в единственном экземпляре
        //эмпти - чтоб не пользоваться нуллами
        private var observer: UiObserver<T> = UiObserver.Empty()

        override fun clear() {
            cache = empty
        }

        @MainThread
        //вызывается  обзерваблом ака мэйнрепрезентативом
        //на ONRESUME каждый раз
        //походу, я понял. этот метод нужен для того, чтобы обновить обзервер вот здесь!!!!!
        //типо ты его обновляешь(типо он тут другой становится, тип)
        // и в зависимости от того  какой у тебя обзервер , дёргается тот
        //update (), который нужно
        override fun updateObserver(uiObserver: UiObserver<T>) = synchronized(lock) {
            observer =
                uiObserver //ключевой момент!!!//апдейт обзервер нужен для связи обзервабла(аппл) и
            //обзервера(активити/фрагмент)//тут и происходит это выше
            //и не только, потому что в зависимости от того, какой update()
            // у кого должен вызываться мы и передаём нужный колбэк вьюхи
            if (!observer.isEmpty()) {
                observer.update(cache)

            }


            //раньше было так
            /*//в ебучем котлине здесь проверка кэша на нулл идёт
            //по сути. сохранение надписи в обсервере ака репрезентативе ака аппликашне
            //вььюха пингуется только
            cache?.let {
                //эта фигня дёргает метод в мэйн активити
                //it - это твой кэш , он объект DashboardScreen:Replace
                observer.update(it)
                //кэш держим, пока не обновим
                cache = null
            }*/
        }


        /**
         * called by  model, в том числе (типо тред у нас, то есть другой)
         * **/
        override fun update(data: T) = synchronized(lock) {
            cache = data
            if (!observer.isEmpty()) {
                observer.update(data)
            }
            //раньше было так
            /*
                    //represenative.showdashboard() из активити приходит в мэйнрепрезентатив, там апдейт и приходим сюда,
                    // в  if (будет тру)
                   //потому сохраняем кэш
                    if (observer.isEmpty()) {  //еще не вызвался онрезюм у второй активити //поезд ещё не пришёл
                        //всегда эмпти
                        //но вызвался онпоуз у первой активити
                        //этот кэш мб и хранилища какого-то
                        cache = data //на онкриэйт акивити каждый раз
                        //process death = cache = null
                    } else {//а сюда мы приходим, когда в сабскрипшене нажимаешь на кнопку(representative.subscribe()->
                        // в сабскррепрезентативе снова , как и выше, navigation.update(), приходящий сюда, но уже в else
                        //потому в мэйнактивити(он обсервер) вызываем апдейт
                        //тоже апдейт
                        //cache = null
                        //пингуем обзервер ака активити или фрагмент
                        observer.update(data)
                    }
                    */

        }

        //для синхронайзда
        companion object {
            private val lock = Object()
        }
    }
}

//пингование от обзервера уже активити (типо) через ф -цию
//это наш колбэк активити и его отпрыски типо (фрагменты)
interface UiObserver<T : Any> : UiUpdate<T>, IsEmpty {

    //было так, потом добавили интерфейс
    // fun isEmpty(): Boolean = false

    class Empty<T : Any> : UiObserver<T> {
        override fun isEmpty(): Boolean = true
        override fun update(data: T) = Unit
    }
}

//пингование от обзервабла самому обзерверу
//чтоб не писать фунцию в инт-се выше. сделали новый интерфейс
interface UpdateObserver<T : Any> {
    fun updateObserver(uiObserver: UiObserver<T> = UiObserver.Empty())
}

//T точно не null
//этот отдельно для сегрегации, раньше был частью обсервера
interface UiUpdate<T : Any> {
    fun update(data: T)
}
