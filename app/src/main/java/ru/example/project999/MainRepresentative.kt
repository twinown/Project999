package ru.example.project999

import androidx.annotation.MainThread

interface MainRepresentative {

    @MainThread
    fun startGettingUpdates(callback: UiObserver<Int>)

    @MainThread
    fun stopGettingUpdates()

    @MainThread
    fun startAsync()

    class Base(private val observable: UiObservable<Int>) : MainRepresentative {

        private val thread = Thread {
            Thread.sleep(5_000)
            observable.update(R.string.hello_world)
        }

        override fun startGettingUpdates(callback: UiObserver<Int>) {
            observable.updateObserver(callback)
        }

        override fun stopGettingUpdates() {
            //по дефолту там эмпти в контрукторе
            observable.updateObserver()
            //могло быть и нулл!!!
          //  activityCallback = ActivityCallback.Empty()
        }

        override fun startAsync() {
            thread.start()
        }
    }
}