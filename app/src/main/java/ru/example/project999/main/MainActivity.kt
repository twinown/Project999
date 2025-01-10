package ru.example.project999.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.example.project999.R
import ru.example.project999.core.ProvideRepresentative
import ru.example.project999.core.Representative
import ru.example.project999.core.UiObserver

class MainActivity //(private val st:String)
//так тоже нельзя делать. потому что парсер твой читает файл манифест,оттуда берет имя и создаёт через
//рефлексию класс с таким именем, а ты ещё туда в конструктор кидаешь аргумент какой-то(он не знает , чё туда кидать
//. он xml)
    : AppCompatActivity(), ProvideRepresentative {

    //смотри, тут та х -ня с аттачом, он еще не настал ,потому будет ошибка
    //потому что поля инициализируются в момент инициализации объекта, а объект MainActivity инииализируется лишь в
    //моменте рефлексивного создания при чтении манифеста,а апплкашн не готов ещё, а ты к нему ссылаешься
    // private val appName = application.getString(R.string.app_name)
    //контекст доступен только на окриате

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
        //это анонимный класс - object:ActivityCallback -он держит ссылку на тот класс, В КОТОРОМ НАХОДИТСЯ
        //со фрагментами та же хрень
        activityCallback = object : ActivityCallback {
            //КОГДА ЭТА Ф - ЦИЯ ВЫЗЫВАЕТСЯ ?????? она вызывается после он резюма
            //который дергает метод startgettingupdates, который дёргает
            //updateObserver в Uiobservable, который дёргает метод update() у обзервера
            //коим явлется твой колбэкактивити
            //сюда приходит как раз DashboardScreen :Screen и не только
            override fun update(data: Screen) = runOnUiThread {
                //data это тот кэш,который ты добавил в UiObservable (если речь о первом запуске -DashboardScreen)
                data.show(supportFragmentManager, R.id.container)
                data.observed(representative)
            }
        }

        //если не в первый раз, то этот метод вызовется, но там пустышка будет
        //тк он работает только при первом запуске
        //корочЕ, он тут картинку не делает, он вообще запускает update в uiobservable
        //и кидает в кэш твой DashbordScreen И ВСЁ (зачем ??). непосредственное обновление уже идёт в onResume
        //сюда приходит скрин!!!далее там
        representative.showDashboard(savedInstanceState == null)  //navigation.update(DashboardScreen)

    }

    override fun onResume() {
        //активити дали репрезентативу
        //здесь репрезентатив получает доступ к активити
        //здесь репрезентатив ака аппликашн держит ссылку на активити
        //я репрезентативу даю доступ к активити
        super.onResume()
        Log.d("nn97", "Mainact onresume")

        //тут и вызывается метод показывания фрагмента,вызов, по итогу, update() анонимного объекта активитиколлбэка выше
        representative.startGettingUpdates(activityCallback) // navigation.updateObserver(callback)

    }

    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Mainact onstop")

        representative.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("nn97", "activity onsaveinsst called")
        super.onSaveInstanceState(outState)
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
//держит ссылку на тот класс, в котором находится
interface ActivityCallback : UiObserver<Screen>



