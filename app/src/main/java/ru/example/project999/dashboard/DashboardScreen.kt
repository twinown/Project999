package ru.example.project999.dashboard

import ru.example.project999.main.Screen

//не будет создаваться каждый раз новый объект
object DashboardScreen : Screen.Replace(DashboardFragment::class.java) // object как синглтон