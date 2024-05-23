package ru.example.project999.core

//чтоб его использвать в наших стейтах, а не целые классы
//для избавления от андроид-зависимостей во фрагментах
interface HideAndShow {

    fun show()
    fun hide()

}