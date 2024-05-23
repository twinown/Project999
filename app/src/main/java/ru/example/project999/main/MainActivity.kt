package ru.example.project999.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.example.project999.R
import ru.example.project999.core.ProvideRepresentative
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver

class MainActivity : AppCompatActivity(), ProvideRepresentative {

    private lateinit var representative: MainRepresentative
    private lateinit var activityCallback: ActivityCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //инициализация дерева вьюх
        Log.d("nn97", "savedinstancestate Mainacivity $savedInstanceState")
        //двойная связь
        //тут актвити связывется с репрезентативом
        //здесь активити получает доступ к репрезентативу
        //я активити даю доступ к репрезентативу
        //репрезентатив дали активити
        //этот метод вызывается в аппликашне
        representative = provideRepresentative(MainRepresentative::class.java)

        //обзервер = колбэк = активити..грубо прям говоря
        activityCallback = object : ActivityCallback {
            //КОГДА ЭТА Ф - ЦИЯ ВЫЗЫВАЕТСЯ ?????? она вызывается после он резюма
            //который дергает метод startgettingupdates, который дёргает
            //updateObserver в Uiobservable, который дёргает метод update() у обзервера
            //коим явлется твой колбэкактивити
            //сюда приходит как раз DashboardScreen :Screen
            override fun update(data: Screen) = runOnUiThread {
                data.show(supportFragmentManager, R.id.container)
            }
        }

        //если не первый раз, то этот метод вызовется, но там пустышка будет
        //тк он работает только при первом запуске
        //корочЕ, он тут картинку не делает, он вообще запускает update  в uiobservable
        //и кидает в кэш твой DashbordScreen И ВСЁ непосредвенное обновление уже идёт в onResume

        //сюда приходдит скрин!!!далее там
        representative.showDashboard(savedInstanceState == null)  //navigation.update(DashboardScreen)

    }

    override fun onResume() {
        //активити дали репрезентативу
        //здесь репрезентатив получает доступ к активити
        //здесть репрезентатив ака аппликашн держит ссылку на активити
        //я репрезентативу даю доступ к активити
        super.onResume()
        Log.d("nn97", "Mainact onresume")

        //тут и вызывается метод показывания фрагмента
        representative.startGettingUpdates(activityCallback) // navigation.updateObserver(callback)

    }

    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Mainact onstop")

        representative.stopGettingUpdates()
    }

    //вызывается выше
    // почему потом эта функция дёргается ещё пару раз ??
    //потому что в базовом фрагменте эта функция вызывается у активити
    //создание объекта мэйнрепрезентатива
    //и не только его, при создании репрезентативов у фрагментов сюда тоже приходит, отсюда в аппликашн
    override fun <T : Representative<*>> provideRepresentative(clasz: Class<T>): T =
        (application as ProvideRepresentative).provideRepresentative(clasz)

}

//ты не передаёшь прям активити, по факту это просто типо интерфейс,живущий с ним что ли
//это неявное активити..почему ? да потому что объект класса эмпти через интерфейс ты создаёшь
//в классе активити..соответсвенно, он живёт столько же,скока активити
//потому и может ТИПО быть активити
interface ActivityCallback : UiObserver<Screen>



