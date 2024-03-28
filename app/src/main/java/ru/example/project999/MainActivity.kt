package ru.example.project999

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity {

    //смотри, тут та х -ня с аттачом, активити с апликашнои не аттачилось ещё тут ,потому будет ошибка
      //private val appName = application.getString(R.string.app_name)

    constructor() {
        Log.d("nn97", "MainAct constr")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("nn97", "MainAct onCreate $savedInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("nn97", "configChanged $newConfig")
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Log.d("nn97","landscape")
        }
    }
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
      //то можешь только в нём с ним и работать
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
/*
object X{
        var a : Activity? = null
}*/

//основное,че ты будешь юзать
//activity
//application
//fragment
//customview