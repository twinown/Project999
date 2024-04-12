package ru.example.project999

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var representative: MainRepresentative
    private lateinit var textView: TextView

    private val  activityCallback = object :ActivityCallback{
        override fun isEmpty(): Boolean  = false

        override fun updateUi() {
            runOnUiThread {
                textView.setText(R.string.hello_world)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //инициализация дерева вьюх

        //активити получил ссылку на репрезентатив
         representative = (application as App).mainRepresentative
         textView = findViewById(R.id.counterTextView)



        //handle сохранения переменной без Bundle и Application через freezes text
        //хотя, если капнуть. он ичпользует онсэйвинстатнстейт всё равно..каждая вью использует
        //тему ниже используй , если у тебя просто текст, цифра
        if (savedInstanceState == null) {
      //первый запуск
            textView.text = "0"
        }
        textView.setOnClickListener {
          representative.startAsync()
        }

    }

    //репрезентатив получил сслыку на активити
    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }


}

//ты не передаёшь прям активити, по факту это просто типо интерфейс,живущий с ним что ли
interface ActivityCallback {

    fun isEmpty(): Boolean
    fun updateUi()
    class Empty : ActivityCallback {
        override fun isEmpty(): Boolean = true

        override fun updateUi() = Unit

    }
}



