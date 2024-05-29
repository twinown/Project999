package ru.example.project999.subscription

import android.os.Bundle
import android.util.Log
import android.view.View
import ru.example.project999.R
import ru.example.project999.core.CustomButton
import ru.example.project999.core.CustomProgressBar
import ru.example.project999.core.UiObserver
import ru.example.project999.main.BaseFragment

class SubscriptionFragment :
    BaseFragment<SubscriptionRepresentative>(R.layout.fragment_suscription) {

    override val clasz = SubscriptionRepresentative::class.java

    private lateinit var observer: UiObserver<SubscriptionUiState>//для стейта

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("nn97", " SubscriptionFragment onViewCreated ")
        super.onViewCreated(view, savedInstanceState)
        val subscribeButton = view.findViewById<CustomButton>(R.id.subscribe_button)
        val progressBar = view.findViewById<CustomProgressBar>(R.id.progressBar)
        val finishButton = view.findViewById<CustomButton>(R.id.finish_button)
        subscribeButton.setOnClickListener {
            representative.subscribe()
        }
        finishButton.setOnClickListener {
            representative.finish()
        }

        observer = object : UiObserver<SubscriptionUiState> {
            override fun update(data: SubscriptionUiState) =
                requireActivity().runOnUiThread {

                }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("nn97", "Subscription fragment onResume")
        representative.startGettingUpdates(observer)
    }

    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Subscription fragment onPause")
        representative.stopGettingUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("nn97", "Subscription fragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nn97", "Subscription fragment onDestroy")
    }
}