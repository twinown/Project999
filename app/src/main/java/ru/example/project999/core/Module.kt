package ru.example.project999.core

//модуль который порождает вмку/репрезентатив. просто выносим порождение объекта в отдельный класс
interface Module<T : Representative<*>> {

    fun representative(): T
}