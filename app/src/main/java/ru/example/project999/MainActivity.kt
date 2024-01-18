package ru.example.project999

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
//здесь ты держишь ссылку на активити вне активити.потому хип захломляется
/*
object X{
        var a : Activity? = null
}*/