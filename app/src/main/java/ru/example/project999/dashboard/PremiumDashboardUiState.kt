package ru.example.project999.dashboard

import ru.example.project999.core.HideAndShow

interface PremiumDashboardUiState {

    fun show(button: HideAndShow, text: HideAndShow)

    object Playing : PremiumDashboardUiState {
        override fun show(button: HideAndShow, text: HideAndShow) {
            button.hide()
            text.show()
        }

    }
}