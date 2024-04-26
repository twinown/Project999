package ru.example.project999.core

import android.app.Application
import android.util.Log

class App : Application(), ProvideRepresentative, CleanRepresentative {

    private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()

    private lateinit var core: Core
    private lateinit var factory: ProvideRepresentative.Factory

    private val handleDeath = HandleDeath.Base()
    private var localCache = ""

    override fun onCreate() {
        super.onCreate()
        core = Core.Base(this)
        factory = ProvideRepresentative.Factory(core, this)
    }

    override fun clear(clasz: Class<out Representative<*>>) {
        representativeMap.remove(clasz)
    }

    //* - что угодно
    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
        return if (representativeMap.containsKey(clasz)) {
            representativeMap[clasz] as T
        } else {
            val representative = factory.provideRepresentative(clasz)
            representativeMap[clasz] = representative
            representative
        }
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