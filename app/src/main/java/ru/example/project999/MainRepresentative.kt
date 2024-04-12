package ru.example.project999

interface MainRepresentative {

    fun startGettingUpdates(callback: ActivityCallback)
    fun stopGettingUpdates()
    fun startAsync()
    fun saveState()

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
                activityCallback = callback // зачем это ?
                callback.updateUi()
                needToPing = false
            } else{
                //вот тут и происходит свзяь репрезентатива с активити
                //в твой репрезентатив(ака аппликашн) приъодит коллбэк(ака активти)
                //и ты гришь ,что твой локальный коллбэк становится пришедшим коллбэком
                activityCallback = callback
            }
        }

        override fun stopGettingUpdates() {
            activityCallback = ActivityCallback.Empty()
        }

        override fun startAsync() {
            thread.start()
        }

        override fun saveState() {
            TODO("Not yet implemented")
        }

    }
}