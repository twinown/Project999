package ru.example.project999.dashboard

import android.view.View
import android.widget.Button
import android.widget.TextView

interface PremiumDashboardUiState {

    fun show(button: Button, textView: TextView)

    object Playing : PremiumDashboardUiState {
        override fun show(button: Button, textView: TextView) {
            button.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }

    }
}