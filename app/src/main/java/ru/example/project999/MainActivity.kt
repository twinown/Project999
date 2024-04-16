package ru.example.project999

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var representative: MainRepresentative
    private lateinit var textView: TextView


    //внутренний объект активити - он типо и есть активити для нас
    //анонимный объект держит ссылку на мэйн активити
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

        //двойная связь/связанность
        //связь активити с репрезентативом (из активити доступ к репрезентативу)
        //репрезентатив приходит в активити(активити получает ссылку на репрезентатив)
         representative = (application as App).mainRepresentative
         textView = findViewById(R.id.counterTextView)



        //handle сохранения переменной без Bundle и Application через freezes text
        //хотя, если капнуть. он ичпользует онсэйвинстатнстейт всё равно..каждая вью использует
        //тему ниже используй , если у тебя просто текст, цифра
        if (savedInstanceState == null) {
      //первый запуск
            textView.text = "0"
        }
        // в ебучем котле здесь и инициализаци и заюивание листенера происходит,потому и непонятно
        textView.setOnClickListener {
          representative.startAsync()
        }

    }

    override fun onResume() {
        //связь репрезентатива с активити
        //активити приходит в репрезентатив(репрезентатив получает ссылку на активити)
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        representative.saveState()
    }

}

//это неявное активити..почему ? да потому что объект класса эмпти через интерфейс ты создаёшь
//в классе активити..соответсвенно, он живёт столько же,скока активити
//потому и может ТИПО быть активити
interface  ActivityCallback{

    fun isEmpty():Boolean
    fun updateUi()
    class Empty:ActivityCallback{
        //умерла ли активити уже?
        override fun isEmpty(): Boolean = true

        override fun updateUi() = Unit

    }
}



