package ru.example.project999.core

import android.content.Context
import android.content.SharedPreferences
import ru.example.project999.main.Navigation

//создание  нашего шэрд преференса
interface ProvideSharedPreferences {

    fun sharedPreferences(): SharedPreferences

}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

//кор - ядро, то есть некоторые общие вещи
interface Core : ProvideNavigation, ProvideSharedPreferences {

    class Base(private val context: Context) : Core {

        private val navigation = Navigation.Base()

        //метод , вызываемый во всех модулях
        override fun navigation(): Navigation.Mutable = navigation

        //вызывается метод в модулях
        override fun sharedPreferences(): SharedPreferences {
            //посотри сам , чё за MODE
            //создание через контекст
            return context.getSharedPreferences("project999", Context.MODE_PRIVATE)
        }
    }
}