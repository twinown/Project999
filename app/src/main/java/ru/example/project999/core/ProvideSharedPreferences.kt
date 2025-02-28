package ru.example.project999.core

import android.content.Context
import android.content.SharedPreferences
import ru.example.project999.main.Navigation

//создание  нашего шэрд преференса здесь !!!
interface ProvideSharedPreferences {

    fun sharedPreferences(): SharedPreferences

}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideRunAsync{
    fun runAsync():RunAsync
}

//кор - ядро, то есть некоторые общие вещи
interface Core : ProvideNavigation, ProvideSharedPreferences,ProvideRunAsync {

    class Base(private val context: Context) : Core {

        private val runAsync = RunAsync.Base(DispatchersList.Base())
        //этот нав_бэйз будет всегда один и тот же везде при создании репрезентативов, поэтому когда ты вызываешь
        //у него updateObserver()(только в майнактивити) или update(), то всегда будет дёргаться
        //observer = mainActivity$onCreate
        private val navigation = Navigation.Base()

        //метод , вызываемый во всех модулях//изза этого метода навигашн один и тот же
        //он создался выше
        override fun navigation(): Navigation.Mutable = navigation

        //вызывается метод в модулях
        override fun sharedPreferences(): SharedPreferences {
            //посотри сам , чё за MODE
            //создание через контекст
            return context.getSharedPreferences("project999", Context.MODE_PRIVATE)
        }

        override fun runAsync(): RunAsync = runAsync
    }
}