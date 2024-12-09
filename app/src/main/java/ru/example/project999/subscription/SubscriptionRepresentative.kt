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


    //инит - это рестор из бандла /getSerializable()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)
    //было ещё так
    //сэйв- это putSerializable()
    // fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)


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

        override fun observed() = observable.clear()



        //restoreState: SaveAndRestoreSubscriptionUiState.Restore - это обертка над бандлом
        override fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            //топ хэндлер смерти процесса
            if (restoreState.isEmpty()) { //тут возвращаем тру или фолс, потому что во фрагменте мы
                //передали bundle,а в SaveAndRestoreState  override fun isEmpty(): Boolean = bundle == null
                Log.d("nn97", "this is very first opening the app")
                handleDeath.firstOpening()
                //этот метод вызывается только для того, чтобы в кэш записать SubscriptionUiState.Initial (при первом открытии)
                observable.update(SubscriptionUiState.Initial)
            } else {
                if (handleDeath.didDeathHappen()) {
                    //go to permanent storage and get localCache
                    Log.d("nn97", "death happened")
                    handleDeath.deathHandled() //флаг ,как я понял
                    //рестор вызывается после смерти
                    //из ресторстейта мы получаем  наш юай стейт(из бандла!!!)
                    val uiState = restoreState.restore()
                    Log.d("nn97", "SubscriptionRepresentative#restoreAfterDeath")
                    Log.d("nn97", "got from restore $uiState")
                    //по дефолту он  там дёргает  observable.update(this),но зачем это делать->
                    //чтоб показать картинку, дебил
                    uiState.restoreAfterDeath(this, observable)
                    //success case observable.update(SubscriptionUiState.Success)
                }
                /* else {
                    //use local cache and dont use permanent
                    Log.d("nn97", "just activity recreated")
                }*/
            }
        }


        //мы сохраняем наш юай стейт. но он находится в обзервабле, поэтому дополняем наш обзёрвабл функцией save
        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        private fun makeThread() = Thread {
            Thread.sleep(3000)
            //на клике идёт сохранение в шердпреф
            //        userPremiumCache.saveUserPremium()
            Log.d("nn97", "death can happen here/  before showing success")
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
            makeThread().start() //старт asynch
        }

        override fun finish() {
            //переход на дэшборд. дёргается в мэйнактивити, видишь. тут навигашн
            // TODO: КАКИМ ОБРАЗОМ ЭТО ПЕРЕЙДЁТ ЕСЛИ В UIOBSERABLE observer -
            //  это твой  object : UiObserver<SubscriptionUiState>
            // TODO: и его update переопределён во фрагменте..для перехода нужен активитиобзервер,ведь там show()
            //короче, баклан, смотри, твои обзерваблы все и навигашн - по факту классы .Base(), нааследующиеся от UiObservable
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

//это сделано для удобства, тк сэйв дёргается в разных местах//зачеммм???затем,что
// //мы сохраняем наш юай стейт всего экрана. но он находится в обзервабле, поэтому дополняем наш обзёрвабл функцией save
interface SaveSubscriptionUiState {
    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)
}

//эти внизу - интерфейс сигригашн, чтоб SubscriptionUiState у репрезентатива ьыли только эти методы
interface SubscriptionObserved {
    fun observed()
}

interface SubscriptionInner {
    fun subscribeInner()
}