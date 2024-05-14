package ru.example.project999.main

import android.content.SharedPreferences

//отсюда начали
//2 действия - изначально, чё нам надо
//создание шерда
interface UserPremiumCache {

    interface Save {
        fun saveUserPremium()
    }

    interface Read {
        fun isUserPremium(): Boolean
    }

    interface Mutable : Save, Read

    class Base(
        private val sharedPreferences: SharedPreferences,
        private val key: String = "isUserPremium"
    ) : Mutable {

        override fun saveUserPremium() {
            sharedPreferences.edit().putBoolean(key, true).apply()
        }

        override fun isUserPremium(): Boolean =
            sharedPreferences.getBoolean(key, false)
    }
}