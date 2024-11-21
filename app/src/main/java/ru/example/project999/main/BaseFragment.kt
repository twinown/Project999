package ru.example.project999.main

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.example.project999.core.ProvideRepresentative
import ru.example.project999.core.Representative

abstract class BaseFragment<T : Representative<*>>(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected lateinit var representative: T


    //то, что классz это  DashboardRepresentative выяснили в fragmentManager.beginTransaction() в инт-се Screen
    protected abstract val clasz: Class<T>

    //этот он криат вызывыается у всех твоих фрагментов каждый раз . когда ты просишь открыть фрагмент
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("nn97", "BaseFragment OnCreate && provideRepresentative")
        super.onCreate(savedInstanceState)
        //общая команда на создание объекта репрезнтатива во фрагментах твоих
        //в активити ты создаёшь об-т репрезентатива и во фрагментах тоже
        //здесь полиморфизм снова:ф-ция Oncreate сначала вызывается у дэшфрагмента, там все переменные
        //переопределены, потому твой clasz - это DashboardRepresentative::class.java
        //этот метод вызывается у активити, посмотри
        representative = (requireActivity() as ProvideRepresentative)
            //когда дело касается дэшборда там дальше решается премиум или базовый
            //DashboardModule(core).representative()
            .provideRepresentative(clasz) //вызывается в активити сначала, оттуда в аппликашне
        //requireActivity() метод вызывает искл при нулле в отличии
        // от getActivity()
    }
}