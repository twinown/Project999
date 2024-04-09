package ru.example.project999

interface MainRepresentative {

    fun startGettingUpdates(callback: ActivityCallback)
    fun stopGettingUpdates()
    fun startAsync()

    class Base : MainRepresentative {
        private var activityCallback: ActivityCallback = ActivityCallback.Empty()
        private var needToPing = false

        private val thread = Thread { //как связаться с активити ??
            Thread.sleep(5_000)
            if (activityCallback.isEmpty()) {
                needToPing = true
            } else {
                activityCallback.updateUi()
            }

        }

        override fun startGettingUpdates(callback: ActivityCallback) {
            if (needToPing){
                callback.updateUi()
                needToPing = false
            } else{
                activityCallback = callback
            }
        }

        override fun stopGettingUpdates() {
            activityCallback = ActivityCallback.Empty()
        }

        override fun startAsync() {
            thread.start()
        }

    }
}