package ru.example.project999.core

import android.app.Application
import android.util.Log


//он как будто сервис-локатор

class App : Application(), ProvideRepresentative, ClearRepresentative {

    //создание репрезентативов в фабрике
    //паттерн наблюдатель: управление обзерверами ака репрезентативами всех вьюх
    //происходит отсюда, аппликашн ака обсервабл
    private lateinit var factory: ProvideRepresentative.Factory

    //private var localCache = ""

    override fun onCreate() {
        Log.d("nn97", "откуда старт после смерти , отсюда, с аппл он криэйта ?")
        super.onCreate()
        //кор кор - это, вообще,интерфейс, кор.Бэйз - это класс, имплем-щий провайднавигашн и провайдШердпреф
        //здесь фабрика
        factory = ProvideRepresentative.Factory(
            ProvideRepresentative.MakeDependency(
                Core.Base(this),
                this
            )
        )
    }

    override fun clear(clasz: Class<out Representative<*>>) {
        factory.clear(clasz)
    }

    //* - что угодно
    //это нужно , чтоб при поворотах экрана мы использовали тот же репрезентатив
    //сюда из активити ВСЕГДА, даже при вызове из фрагментов
    //потому хранится в алликашне
    //сервис локатор вот он
    //связка аппликашн(этот метод)+фабрика+лист
    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
        Log.d("nn97", "provideRepresentative() в аппликашне")
        return factory.provideRepresentative(clasz)
    }
}