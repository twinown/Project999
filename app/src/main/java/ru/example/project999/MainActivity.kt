package ru.example.project999

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var representative: MainRepresentative
    private lateinit var activityCallback: ActivityCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //инициализация дерева вьюх

        //двойная связь
        //тут актвити связывется с репрезентативом
        //здесь активити получает доступ к репрезентативу
        //я активити даю доступ к репрезентативу
        //репрезентатив дали активити
        representative = (application as App).mainRepresentative

        val textView = findViewById<TextView>(R.id.counterTextView)
        //handle сохранения переменной без Bundle и Application через freezes text
        //хотя, если капнуть. он использует онсэйвинстатнстейт всё равно..каждая вью использует
        //тему ниже используй , если у тебя просто текст, цифра
        if (savedInstanceState == null) {  //первый запуск
            textView.text = "0"
        }

        activityCallback = object : ActivityCallback {
            override fun update(data: Int) = runOnUiThread {
                textView.setText(data)
            }

        }

        textView.setOnClickListener {
            representative.startAsync()
        }
    }

    override fun onResume() {
        //активити дали репрезентативу
        //здесь репрезентатив получает доступ к активити
        //здесть репрезентатив ака аппликашн держит ссылку на активити
        //я репрезентативу даю доступ к активити
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }
}

//ты не передаёшь прям активити, по факту это просто типо интерфейс,живущий с ним что ли
//это неявное активити..почему ? да потому что объект класса эмпти через интерфейс ты создаёшь
//в классе активити..соответсвенно, он живёт столько же,скока активити
//потому и может ТИПО быть активити
interface ActivityCallback : UiObserver<Int>



