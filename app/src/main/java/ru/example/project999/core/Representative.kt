package ru.example.project999.core

interface Representative<T : Any> {

    fun startGettingUpdates(callback: UiObserver<T>) = Unit

    fun stopGettingUpdates() = Unit

    //топ хэндлер смерти процесса
    //   private val handleDeath = HandleDeath.Base()
    /*  fun activityCreated(firstOpening: Boolean) {
          if (firstOpening) {
              //init local cache
              localCache = "a"
              Log.d("nn97", "this is very first opening the app")
              handleDeath.firstOpening()
          } else {
              if (handleDeath.wasDeathHappened()) {
                  //go to permanent storage and get localCache
                  Log.d("nn97", "death happened")
                  handleDeath.deathHandled()
              } else {
                  //use local cache and dont use permanent
                  Log.d("nn97", "just activity recreated")
              }
          }
      }*/
}