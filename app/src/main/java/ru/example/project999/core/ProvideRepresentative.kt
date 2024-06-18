package ru.example.project999.core

import ru.example.project999.dashboard.DashboardModule
import ru.example.project999.dashboard.DashboardRepresentative
import ru.example.project999.main.MainModule
import ru.example.project999.main.MainRepresentative
import ru.example.project999.subscription.SubscriptionModule
import ru.example.project999.subscription.SubscriptionRepresentative

//поставщик репрезентативов, как я понял//фабрика объектов
interface ProvideRepresentative {

    //сюда из аппликашна
    //сюда из фрагментов через активити
    //короче - это же ФАБРИКА репрезентативов, значит, ты сюда будешь приходить и для
    //создания мэйн репра, дашборд репра, сабскрип репра
    //вызывается из аппликашна
    fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T

    class Factory(
        private val core: Core,
        private val clear: CleanRepresentative
    ) : ProvideRepresentative {
        override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
            return when (clasz) { //по интерфейсу отдаёт класс из модуля
                MainRepresentative::class.java -> MainModule(core).representative()
                //ниже первое обращение к шэрдам
                DashboardRepresentative::class.java -> DashboardModule(core).representative()
                SubscriptionRepresentative::class.java -> SubscriptionModule(
                    core,
                    clear
                ).representative()

                else -> throw IllegalStateException("unknown class $clasz")
            } as T
        }
    }
}