package ru.example.project999.core

import android.app.Application

class App : Application(), ProvideRepresentative, CleanRepresentative {

    //хранение репрезентативов
    //надо чистить
    private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()

    private lateinit var core: Core

    //создание репрезентативов в фабрике
    private lateinit var factory: ProvideRepresentative.Factory

    //private var localCache = ""

    override fun onCreate() {
        super.onCreate()
        core = Core.Base(this) //синглтон объект(привязан к аппликашну)
        //здесь фабрика
        factory = ProvideRepresentative.Factory(core, this)
    }

    override fun clear(clasz: Class<out Representative<*>>) {
        representativeMap.remove(clasz)
    }

    //* - что угодно
    //это нужно , чтоб при поворотах экрана мы использовали тот же репрезентатив
    //потому хранится в алликашне
    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
        return if (representativeMap.containsKey(clasz)) {
            representativeMap[clasz] as T
        } else {
            val representative = factory.provideRepresentative(clasz)
            representativeMap[clasz] = representative   //тот же map.put()
            representative
        }
    }


}