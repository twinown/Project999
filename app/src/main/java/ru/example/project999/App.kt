package ru.example.project999

import android.app.Application
import android.util.Log

class App : Application() {

      lateinit var mainRepresentative: MainRepresentative
    //count живёт теперь в аппликашне,потому ему пофиг на повороты
    //каунт будет занулляться,если умрёт аппликашн!а аппл умирает при смерти процееса
    //var count = 0
    private val handleDeath = HandleDeath.Base()
    private var localCache = ""

       override fun onCreate() {
        super.onCreate()
           mainRepresentative = MainRepresentative.Base(UiObservable.Single())
    }

    //топ хэндлер смерти процесса
    fun activityCreated(firstOpening: Boolean) {
        if (firstOpening) {
            //init local cache
            localCache = "a"
            Log.d("nn97", "this is very first opening the app")
            handleDeath.firstOpening()
        } else {
            if (handleDeath.wasDeathHappened()) {
                //go to permanent storage and get localCache
                Log.d("nn97", "death happened")
                handleDeath.deathHandled()
            } else {
                //use local cache and dont use permanent
                Log.d("nn97", "just activity recreated")
            }
        }

    }
}