package ru.example.project999

interface MainRepresentative {

    fun startGettingUpdates(callback: ActivityCallback)
    fun stopGettingUpdates()
    fun startAsync()
    fun saveState()

    class Base : MainRepresentative {
        private var activityCallback: ActivityCallback = ActivityCallback.Empty()
        //флаг на обновление текста,если первое активити уже умерло,а второе ещё не создалось
        private var needToPing = false

        private val thread = Thread {
            Thread.sleep(5_000)
            //проверка через  5 сек на живость активити
            //типо после этого жди , пока вторая активити будет готова
            //первый if о том, что онрезюм второй активити ещё не вызван , а онпауз первой уже вызван
            if (activityCallback.isEmpty()) {
                needToPing = true
            } else {
                activityCallback.updateUi()
            }
        }

        //эта хрень вызывается в каждой активити созданной, она же в оnResume()
        //приходящий коллбэк и есть твоя активити (типо)
        override fun startGettingUpdates(callback: ActivityCallback) {
            if (needToPing){
                //данные уже есть, дождались вызлва онрезюм и пошёл этот код
                //это иф вызывается после ифа выше
                activityCallback = callback
                callback.updateUi()
                needToPing = false
            } else{
                activityCallback = callback
            }
        }

        override fun stopGettingUpdates() {
            //могло быть и нулл!!!
            activityCallback = ActivityCallback.Empty()
        }

        override fun startAsync() {
            thread.start()
        }

        override fun saveState() {
        }

    }
}