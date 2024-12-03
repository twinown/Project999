package ru.example.project999.subscription

import org.junit.Before
import org.junit.Test
import ru.example.project999.core.HandleDeath

//тестирование джава/котлин кода ВНЕ андроида
//в юнит-тестах в конструкторы мы принимаем интерфейсы!
//а в функции или примитивы или интерфейсы!
class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative

    @Before
    fun setup() {
        //     representative = SubscriptionRepresentative.Base(FakeHandleDeath.Base())
    }

    //короче, смотри, в репрезентативе, вызывая эту функцию, ты мог передать обычный бандл, который
    //возвращал бы тебе тру или фолс, но при выполнении тестов вот тут ты не можешь закинуть бандл (почему?потому что юнит тесты - это
    // тестирование джава/котлин кода ВНЕ андроида, а бандл - это ос андроид)
    //потому сделали обёртку, которая возвращает нам из обёртки над бандлом просто тру или фолз.
    //то есть мы можем просто здесь написать фейковые интерфейсы, типо выполняющие роль бандла
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