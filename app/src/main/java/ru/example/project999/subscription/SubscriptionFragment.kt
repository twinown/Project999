package ru.example.project999.subscription

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import ru.example.project999.R
import ru.example.project999.core.ProvideRepresentative
import java.util.zip.Inflater

class SubscriptionFragment : Fragment() {

    private lateinit var subscriptionRepresentative: SubscriptionRepresentative

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscriptionRepresentative = (requireActivity() as ProvideRepresentative)
            .provideRepresentative(SubscriptionRepresentative::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suscription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.subscribe_button)
        button.setOnClickListener {
            subscriptionRepresentative.subscribe()
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    subscriptionRepresentative.clear()
                }
// TODO: 2.27.03 
            })
    }


    override fun onResume() {
        super.onResume()
        Log.d("nn970", "Subscription fragment onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nn970", "Subscription fragment onDestroy")

    }
}