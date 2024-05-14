package ru.example.project999.main

import ru.example.project999.core.Core
import ru.example.project999.core.Module

//мэйн модуль для создания объекта мэйн репрезентатива
class MainModule(private val core: Core) : Module<MainRepresentative> {

    override fun representative(): MainRepresentative = MainRepresentative.Base(core.navigation())
}