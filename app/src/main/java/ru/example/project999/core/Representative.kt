package ru.example.project999.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//интерфейс маркер/маркеровочный
interface Representative<T : Any> {

    //юнит - для тех репрезентативов и их фрагментов. которым  не нужно обновлять себя!!!!!
    //метод так называется , потому что мы начинаем получать обновления для нашего фрагмента
    fun startGettingUpdates(callback: UiObserver<T>) = Unit

    fun stopGettingUpdates() = Unit

    abstract class Abstract<T : Any>(
        private val runAsync: RunAsync
    ) : Representative<T> {
        private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        protected fun <T : Any> handleAsync(
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            runAsync.runAsync(coroutineScope, backgroundBlock, uiBlock)
        }
        protected  fun clear(){
            runAsync.clear()
        }
    }
}

interface RunAsync {

    fun <T : Any> runAsync(
        scope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    )

    fun clear()
    class Base(
        private val dispatchersList: DispatchersList
    ) : RunAsync {
        private var job:Job?= null
        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
           job = scope.launch(dispatchersList.background()) {
                val result = backgroundBlock.invoke()
                withContext(dispatchersList.ui()) {
                    uiBlock.invoke(result)
                }

            }

        }

        override fun clear() {
            job?.cancel()
            job = null
        }
    }
}