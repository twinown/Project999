package ru.example.project999.subscription

import android.util.Log
import androidx.annotation.MainThread
import ru.example.project999.core.CleanRepresentative
import ru.example.project999.core.HandleDeath
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver
import ru.example.project999.dashboard.DashboardRepresentative
import ru.example.project999.dashboard.DashboardScreen
import ru.example.project999.main.Navigation
import ru.example.project999.main.UserPremiumCache

interface SubscriptionRepresentative : Representative<SubscriptionUiState>,
//interface segregation here
    SaveSubscriptionUiState, SubscriptionObserved, SubscriptionInner {


    @MainThread
    fun subscribe()
    fun finish()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)


    class Base(

        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val clear: CleanRepresentative,
        private val userPremiumCache: UserPremiumCache.Save,
        private val navigation: Navigation.Update
    ) : SubscriptionRepresentative {

        //init блок = конструктор
        //первичный конструктор уже есть
        init {
            Log.d("nn97", "SubscriptionRepresentative init")
        }

        override fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            //топ хэндлер смерти процесса
            if (restoreState.isEmpty()) {
                Log.d("nn97", "this is very first opening the app")
                handleDeath.firstOpening()
                observable.update(SubscriptionUiState.Initial)
            } else {
                if (handleDeath.didDeathHappen()) {
                    //go to permanent storage and get localCache
                    Log.d("nn97", "death happened")
                    handleDeath.deathHandled() //флаг ,как я понял
                    //рестор после смерти
                    //по дефолту он  там дёргает  observable.update(this),но зачем это делать
                    //когда в эмпти (в сабюайстейте) он кидает юнит, но чтоб от Initial пришли к  Empty
                    //надо эмпти пингануть, то есть нужно зачистить, а зачистка происходит в методе observed()->clear()
                    val uiState = restoreState.restore()
                    Log.d("nn97", "SubscriptionRepresentative#restoreAfterDeath")
                    uiState.restoreAfterDeath(this, observable)
                    //success case    observable.update(SubscriptionUiState.Success)
                }/* else {
                    //use local cache and dont use permanent
                    Log.d("nn97", "just activity recreated")
                }*/
            }
        }

        override fun observed() = observable.clear()

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        private val thread = Thread {
            Thread.sleep(3000)
            //на клике идёт сохранение в шердпреф
            userPremiumCache.saveUserPremium()
            observable.update(SubscriptionUiState.Success)
        }


        override fun subscribe() {
            observable.update(SubscriptionUiState.Loading)
            //здесь мб лаг..может произойти смерть процесса здесь
            Log.d("nn97", "death can happen here//subscribe(), but before thread.start()")
            subscribeInner()
        }

        override fun subscribeInner() {
            Log.d("nn97", "SubscriptionRepresentative#subscribeInner")
            thread.start() //старт asynch
        }

        override fun finish() {
            //переход на дэшборд. дёргается в мэйнактивити, видишь. тут навигашн
            // TODO: КАКИМ ОБРАЗОМ ЭТО ПЕРЕЙДЁТ ЕСЛИ В UIOBSERABLE observer -
            //  это твой  object : UiObserver<SubscriptionUiState>
            // TODO: и его update переопределён во фрагменте..для перехода нужен активитиобзервер,ведь там show()
            //короче, баклан, смотри, твои обзерваблы все и навигашн - пофакту классы .Base(), нааследующиеся от UiObservable
            //то есть это отдельные классы , реализующие методы те все, что в UiObservable,они , по итогу, сохраняют все данные в нём, пока не пере-
            //пишешь. то есть в navigation.Base():UiObserable в observer хранится MainActivity$OnCreate, пока не перепишешь (updateObserver)
            //переписать можно только в MainRepresentative(потому что он Mutable )
            //переходы между фрагментами делает активити колбэк !!!
            //ниже startGettingUpdates ты обновляешь  observable..в navigation.update() как был тем самым из мэйн активити, так и остался
            clear.clear(DashboardRepresentative::class.java)
            clear.clear(SubscriptionRepresentative::class.java)
            //почему observer тут - это  MainActivity
//да потому что navigation - он везде один и тот же, при создании объекта репрезентативов вызывается core.navigation(),
            //а  он в одном экзмепляре,живёт в аппликашне. все данные там и живут, по итогу, потому вот так
            navigation.update(DashboardScreen)
        }

        override fun startGettingUpdates(callback: UiObserver<SubscriptionUiState>) {
            //обноляется именно observable , а не navigation
            observable.updateObserver(callback) //связь фр-та и репрезентатива

        }

        override fun stopGettingUpdates() {
            observable.updateObserver()

        }
    }
}

interface SaveSubscriptionUiState {
    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)
}

interface SubscriptionObserved {
    fun observed()
}

interface SubscriptionInner {
    fun subscribeInner()
}