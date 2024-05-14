package ru.example.project999.core

import android.content.Context
import android.content.SharedPreferences
import ru.example.project999.main.Navigation

//получаем наш шэрд преференс
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

        override fun navigation(): Navigation.Mutable = navigation

        override fun sharedPreferences(): SharedPreferences {
            //посотри сам , чё за MODE
            return context.getSharedPreferences("project999", Context.MODE_PRIVATE)
        }
    }
}