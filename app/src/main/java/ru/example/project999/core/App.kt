package ru.example.project999.core

import android.app.Application
import android.util.Log


//он как будто сервис-локатор

class App : Application(), ProvideRepresentative, CleanRepresentative {

    //хранение репрезентативов
    //надо чистить
    private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()


    private lateinit var core: Core

    //создание репрезентативов в фабрике
    //паттерн наблюдатель: управление обзерверами ака репрезентативами всех вьюх
    //происходит отсюда, аппликашн ака обсервабл
    private lateinit var factory: ProvideRepresentative.Factory

    //private var localCache = ""

    override fun onCreate() {
        super.onCreate()
        //кор кор - это, вообще,интерфейс, кор.Бэйз - это класс, имплем-щий провайднавигашн и провайдШердпреф
        core = Core.Base(this) //синглтон объект(привязан к аппликашну)
        //здесь фабрика
        factory = ProvideRepresentative.Factory(core, this)
    }

    override fun clear(clasz: Class<out Representative<*>>) {
        representativeMap.remove(clasz)
    }

    //* - что угодно
    //это нужно , чтоб при поворотах экрана мы использовали тот же репрезентатив
    //сюда из активити ВСЕГДА, даже при вызове из фрагментов
    //потому хранится в алликашне
    //сервис локатор вот он
    //связка аппликашн(этот метод)+фабрика+лист
    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
        Log.d("nn97", "provideRepresentative() в аппликашне")
        return if (representativeMap.containsKey(clasz)) {
            representativeMap[clasz] as T
        } else {
            val representative = factory.provideRepresentative(clasz)
            representativeMap[clasz] = representative   //тот же map.put()
            representative
        }
    }
}