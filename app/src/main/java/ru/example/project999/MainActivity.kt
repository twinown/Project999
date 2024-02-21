package ru.example.project999

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //смотри, тут та х -ня с аттачом, ты его не сделал ,потому будет ошибка
//    private val appName = application.getString(R.string.app_name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NN97", "thread: ${Thread.currentThread()}")
    }
}
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