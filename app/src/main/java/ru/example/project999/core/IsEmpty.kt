package ru.example.project999.core

interface IsEmpty {
    //по дефолту лучше не делать тут , а в каждом свой дефолт, так как если ты здесь напишешь, тебе в твоих классах
    //никто не скажет это изменить
    fun isEmpty(): Boolean
}