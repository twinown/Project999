package ru.example.project999.main

import ru.example.project999.core.Core
import ru.example.project999.core.Module

//мэйн модуль для создания объекта мэйн репрезентатива
class MainModule(private val core: Core) : Module<MainRepresentative> {

    //тот же навигашн, что и в остальных - он в одном экзмпляре, хранящемся в аппликашне
    //тот же кор всегда, тот же метод navigation()
    override fun representative(): MainRepresentative = MainRepresentative.Base(core.navigation())
}