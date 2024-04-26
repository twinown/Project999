package ru.example.project999.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.example.project999.dashboard.DashboardFragment
import ru.example.project999.subscription.SubscriptionFragment

interface Screen {
    fun show(fragmentManager: FragmentManager, containerId: Int)

    //одновременно живёт несколько фрагментов,предыдущий уходитв стек
    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction().add(containerId, fragmentClass.newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    //out - значит фрагмент и все его наследники иначе будет думать,чо только фрагмент
    //фрагмент прям заменяется (старый удаляется)
    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction().replace(containerId, fragmentClass.newInstance())
                .commit()
        }
    }

    //не будет создаваться каждый раз новый объект
    object Dashboard : Replace(DashboardFragment::class.java) // object как синглтон

    object Subscription : Add(SubscriptionFragment::class.java)
}