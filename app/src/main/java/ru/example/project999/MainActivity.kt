package ru.example.project999

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity {

    //смотри, тут та х -ня с аттачом, активити с апликашном не зааттачилось ещё тут ,потому будет ошибка
    //у тебя здесь ещё активити не стартанула(онкриэйт означает.в тч , что точно вызван кнсрауктор и точно метод аттач),
    // чтоб аппл зааттачился, сначала вызввать констуктор активити. а тут ты до конструктора даже
    // здесь ты обращаешься к аппл-ну до аттачаи и  до вызова констр активити твоей
    //потому тут и нулл: нет аттача - нет инициализации апплакашна
    // private val appName = getApplication().getString(R.string.app_name)

    //для примера про largeheap
    // private val  list = mutableListOf<Any>()
    private var count = 0

    constructor() {
        Log.d("nn97", "MainAct constr")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("nn97", "MainAct onCreate bundle $savedInstanceState")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //инициализация дерева вьюх

        (application as App).activityCreated(savedInstanceState == null)


        val textView = findViewById<TextView>(R.id.counterTextView)


        /*//для примера про largeheap
        while (true){
            list.add(A())
        }*/


        //handle сохранения переменной без Bundle и Application через freezes text
        //хотя, если капнуть. он ичпользует онсэйвинстатнстейт всё равно..каждая вью использует
        //тему ниже используй , если у тебя просто текст, цифра
        if (savedInstanceState == null) {
            textView.text = "0"
        }
        textView.setOnClickListener {
            var count = textView.text.toString().toInt()
            textView.text = (++count).toString()
        }


        //handle смерти процесса через Bundle + onSaveInstanceState (сохранение переменной)
        /*    count = if(savedInstanceState ==null){
                Log.d("nn97", "first time app created")
                0
            } else{
                //бандл новый каждый раз тоже, данные из одного копируются в другой
                Log.d("nn97", "bundle is ${savedInstanceState.hashCode()} тянется поле из savedInstanceState ")
                //возможно, где - то там вызывается onrRestoreInstanceState
                savedInstanceState.getInt(KEY)
            }


            //handle смерти процесса через Bundle + onSaveInstanceState (сохранение переменной)
            //при повороте не сохр значение потому переписали
            //мб setText(Int resid) - ищет в R классе в стрингах стринг с айдишником,
            //который ты передал
            textView.text = count.toString()
            textView.setOnClickListener {
                textView.text =  (++count).toString()
            }*/
    }

    //вызывается перед смертью процесса,либо активити, даже при сворачивании активити
    //после OnStop()
    /* override fun onSaveInstanceState(outState: Bundle) {
         super.onSaveInstanceState(outState)
         Log.d("nn97","onSaveInstanceState method called ${outState.hashCode()}")
         outState.putInt(KEY, count)
     }
     companion object{
         private const val KEY = "myCount"
     }*/


    //  handle смерти активити через аппликашн (сохранение переменной)
    /* textView.text = (application as App).count.toString()
     textView.setOnClickListener {
         textView.text = (++(application as App).count).toString()
     }*/


    //хэндлишь сам моменты , при которых активити умирает через if
    /* override fun onConfigurationChanged(newConfig: Configuration) {
         super.onConfigurationChanged(newConfig)
         Log.d("nn97", "configChanged $newConfig")
         if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
             Log.d("nn97","landscape")
         }
     }*/
    override fun onRestart() {
        super.onRestart()
        Log.d("nn97", "MainAct onRestart")
    }
    override fun onStart() {
        super.onStart()
        Log.d("nn97", "MainAct onStart")
    }


    override fun onResume() {
        super.onResume()
        Log.d("nn97", "MainAct onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.d("nn97", "MainAct onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("nn97", "MainAct onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nn97", "MainAct onDestroy")
    }

}

//Log.d("nn97", "${Thread.currentThread()}")

/* var count = 0
      //закидываем месседж наверх очереди
      //вызывая мэйнлуп - то есть ставим на очередь мэйн треда
      //потому что если ты иниц вьюху свою в мэйн треде
      //то можешь только в нём с ней и работать
      textView.setOnClickListener {
          Thread {
              Handler(Looper.getMainLooper()).post {
                  textView.setText("gogo")
              }

          }.start()
      }

      //та же хрень,что выше, но попроще
      textView.setOnClickListener {
          Thread {
              runOnUiThread { textView.setText("gogo") }
          }.start()
      }

      //кривой таймер
      textView.setOnClickListener {
          object : CountDownTimer(10000, 1000) {
              override fun onTick(millisUntilFinished: Long) {
                  textView.setText((millisUntilFinished/1000).toString())
              }

              override fun onFinish() {
                  textView.append("\nfinished")
              }
          }.start()
      }


      //норм таймер
      textView.setOnClickListener {
          java.util.Timer().scheduleAtFixedRate(object :TimerTask(){
              override fun run() = runOnUiThread {
                 textView.setText((++count).toString())
              }
          },0,1000)
      }

*/


//здесь ты держишь ссылку на активити вне активити.потому хип захломляется
//object - синглтон - статичный объект
//вообще,эту хрень мы не используем в андроиде
//синглтон - это у нас application
//НЕ ХРАНИ ССЫЛКУ НА АКТИВИТИ
/*
object X{
        var a : Activity? = null
}*/

//основное,че ты будешь юзать
//activity
//application
//fragment
//customview


//для примера про largeheap
//data class A(private val l:Long = System.currentTimeMillis())
