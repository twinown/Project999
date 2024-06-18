package ru.example.project999.subscription

import org.junit.Before
import org.junit.Test
import ru.example.project999.core.HandleDeath

class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative

    @Before
    fun setup() {
        representative = SubscriptionRepresentative.Base(FakeHandleDeath.Base())
    }

    @Test
    fun test() {
        representative.init(true)
        representative.init(false)
    }
}

private interface FakeHandleDeath : HandleDeath {

    //имитация смерти процесса
    fun killProcess()

    class Base : FakeHandleDeath {

        private var deathHappened = true
        override fun killProcess() {
            deathHappened = true
        }

        override fun firstOpening() {
            deathHappened = false
        }

        override fun didDeathHappen(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }
}