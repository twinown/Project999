package ru.example.project999.core

interface CleanRepresentative {
    fun clear(clasz: Class<out Representative<*>>)
}