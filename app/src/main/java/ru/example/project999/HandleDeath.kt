package ru.example.project999


//этот интерфейс нужен для того, что понять произошла смерть процесса или просто поворот экрана
interface HandleDeath {
    fun firstOpening()
    fun wasDeathHappened(): Boolean
    fun deathHandled()


    class Base : HandleDeath {

        private var deathHappened = true
        override fun firstOpening() {
            deathHappened = false
        }

        override fun wasDeathHappened(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }

    }
}