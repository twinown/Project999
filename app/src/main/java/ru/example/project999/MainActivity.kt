package ru.example.project999

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    //смотри, тут та х -ня с аттачом, активити с апликашном не аттачилось ещё тут ,потому будет ошибка
    //  private val appName = application.getString(R.string.app_name)

    private val list = mutableListOf<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       findViewById<TextView>(R.id.textView).setOnClickListener {
           while (true)
               list.add(A())
       }
    }

    data class A(private val l: Long = System.currentTimeMillis())
}


/* val textView = findViewById<TextView>(R.id.textView)
 var count = 0*/

//ANR
/* textView.setOnClickListener {
     Thread.sleep(50_000)
     textView.setText("gogo")
 }*/

//закидываем месседж наверх очереди
//вызывая мэйнлуп - то есть ставим на очередь мэйн треда
//потому что если ты иниц вьюху свою в мэйн треде
//то можешь только в нём с ней и работать
/*  textView.setOnClickListener {
      Thread {
          Handler(Looper.getMainLooper()).post {
              textView.setText("gogo")
          }

      }.start()
  }*/

//та же хрень,что выше, но попроще
/*  textView.setOnClickListener {
      Thread {
          runOnUiThread { textView.setText("gogo") }
      }.start()
  }*/

//кривой таймер
/*  textView.setOnClickListener {
      object : CountDownTimer(10000, 1000) {
          override fun onTick(millisUntilFinished: Long) {
              textView.setText((millisUntilFinished/1000).toString())
          }

          override fun onFinish() {
              textView.append("\nfinished")
          }
      }.start()
  }*/


//норм таймер
/*   textView.setOnClickListener {
       java.util.Timer().scheduleAtFixedRate(object :TimerTask(){
           override fun run() = runOnUiThread {
              textView.setText((++count).toString())
           }
       },0,1000)
   }*/


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