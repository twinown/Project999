package ru.example.project999.main

import ru.example.project999.core.Module

class MainModule : Module<MainRepresentative> {
    override fun representative(): MainRepresentative {
        return MainRepresentative.Base(Navigation.Base)
    }
}