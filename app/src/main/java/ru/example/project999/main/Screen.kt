package ru.example.project999.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Screen {
    //вызывается в мэйн активити
    fun show(fragmentManager: FragmentManager, containerId: Int)
    fun observed(representative: MainRepresentative) = representative.observed()
    //одновременно живёт несколько фрагментов,предыдущий уходит в стек
    //сабскрипшн
    abstract class Add(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.newInstance())
                //при эддтубэкстеке предыд фрагмент пересоздаётся заново
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    //out - значит фрагмент и все его наследники иначе будет думать,чо только фрагмент
    //фрагмент прям заменяется (старый удаляется)
    //дашборд
    abstract class Replace(private val fragmentClass: Class<out Fragment>) : Screen {

        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.newInstance())
                .commit()
        }
    }

    //Dashboard -> replace
    //Subscription -> add
    //comeback via navigation#navigate(Screen.Pop)...вообще там update()
    object Pop : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            //при попбэкстеке фрагмент предыд. заново не создается
            fragmentManager.popBackStack()
        }
    }

    //эмпти - плата на нуллабельность
    object Empty : Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) = Unit
    }
}