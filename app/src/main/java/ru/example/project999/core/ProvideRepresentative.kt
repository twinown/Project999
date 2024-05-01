package ru.example.project999.core

import ru.example.project999.dashboard.DashboardModule
import ru.example.project999.dashboard.DashboardRepresentative
import ru.example.project999.main.MainModule
import ru.example.project999.main.MainRepresentative
import ru.example.project999.subscription.SubscriptionModule
import ru.example.project999.subscription.SubscriptionRepresentative

interface ProvideRepresentative {

    fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T

    class Factory(
        private val core: Core,
        private val clear: CleanRepresentative
    ) : ProvideRepresentative {
        override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T {
            return when (clasz) {
                MainRepresentative::class.java -> MainModule(core).representative()
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