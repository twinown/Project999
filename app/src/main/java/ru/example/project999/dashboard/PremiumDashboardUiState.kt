package ru.example.project999.dashboard

import ru.example.project999.core.HideAndShow

//чё просиходит , когда ты премиум..эта фунция вызывается  в дашбордфрагменте при клике
interface PremiumDashboardUiState {

    //видишь, вместо кнопок приходит интерфейс
    fun show(button: HideAndShow, text: HideAndShow)
    fun observed(representative: DashboardRepresentative) = representative.observed()
    object Playing : PremiumDashboardUiState {
        override fun show(button: HideAndShow, text: HideAndShow) {
            //view.gone - убирает вью и его ространство
            //view.invisible - убирает вью и оставляет его пространство
            button.hide()
            text.show()
        }

    }

    object Empty : PremiumDashboardUiState {
        override fun show(button: HideAndShow, text: HideAndShow) = Unit
    }
}