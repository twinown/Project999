package ru.example.project999.main

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.example.project999.core.ProvideRepresentative
import ru.example.project999.core.Representative

abstract class BaseFragment<T : Representative<*>>(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected lateinit var representative: T

    protected abstract val clasz: Class<T>

    //это он криат вызывыается у всех твоих фрагментов каждый раз . когда ты просишь открыть фрагмент
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //общая команда на создание объекта репрезнтатива во фрагментах твоих
        //в активити ты создаёшь об-т репрезентатива и во фрагментах тоже
        representative = (requireActivity() as ProvideRepresentative)
            .provideRepresentative(clasz) //requireActivity() метод вызывает искл при нулле в отличии
        // от getActivity()
    }

}