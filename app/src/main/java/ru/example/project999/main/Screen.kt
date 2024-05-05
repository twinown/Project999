package ru.example.project999.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {
    fun show(fragmentManager: FragmentManager, containerId: Int)

    //одновременно живёт несколько фрагментов,предыдущий уходитв стек
    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction().replace(containerId, fragmentClass.newInstance())
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

}