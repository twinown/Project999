package ru.example.project999

import androidx.annotation.MainThread


interface UiObservable<T : Any> : UiUpdate<T> {

    fun updateObserver(uiObserver: UiObserver<T> = UiObserver.Empty())

    //для того, чтоб кэш не держать долго, а выше было (ты удалил). чтоб кэш пинговался
    //отдача данных один раз в ту же актитвити,короче говоря
    //сингл используется вместе в фризес текст, кэш обнуляется при смерти, а всё живёт за счёт бандла
    class Single<T : Any> : UiObservable<T> {

        @Volatile
        private var cache: T? = null

        @Volatile //чтоб не кэшировалась разными тредами переменная(каждый у себя типо) -
        //в единственном экземпляре
        private var observer: UiObserver<T> = UiObserver.Empty()

        //на он резюме
        @MainThread
        override fun updateObserver(uiObserver: UiObserver<T>) = synchronized(lock) {
            observer = uiObserver
            if (!observer.isEmpty()) {
                //в ебучем котлине здесь проверка кэша на нулл идёт
                //по сути. сохранение надписи в обсервере ака репрезентативе ака аппликашне
                cache?.let {
                    observer.update(it)
                    cache = null
                }
            }
        }

        /**
         * called by  model (типо тред у нас, то есть другой)
         * **/
        override fun update(data: T) = synchronized(lock) {
            if (observer.isEmpty()) {   //еще не вызвался онрезюм у второй активити
                cache = data
                //process death = cache = null
            } else {
                cache = null
                //пингуем обзервер ака активити
                observer.update(data)
            }
        }

        companion object {
            private val lock = Object()
        }
    }
}

//T точно не null
interface UiUpdate<T : Any> {
    fun update(data: T)
}

interface UiObserver<T : Any> : UiUpdate<T> {
    fun isEmpty(): Boolean = false

    class Empty<T : Any> : UiObserver<T> {
        override fun isEmpty(): Boolean = true
        override fun update(data: T) = Unit
    }
}