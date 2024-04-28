package ru.example.project999.core

import android.content.Context
import android.content.SharedPreferences
import ru.example.project999.main.Navigation

interface ProvideSharedPreferences {

    fun sharedPreferences(): SharedPreferences


}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface Core : ProvideNavigation, ProvideSharedPreferences {

    class Base(private val context: Context) : Core {

        private val navigation = Navigation.Base()

        override fun navigation(): Navigation.Mutable = navigation

        override fun sharedPreferences(): SharedPreferences {
            return context.getSharedPreferences("object999", Context.MODE_PRIVATE)
        }
    }
}