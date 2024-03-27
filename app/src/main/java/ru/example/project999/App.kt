package ru.example.project999

import android.app.Application
import android.util.Log

class App : Application {

    constructor() {
        Log.d("nn97", "app constr")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("nn97", "app oncreate")
    }
}