package ru.example.project999.subscription

import android.os.Bundle
import android.util.Log
import android.view.View
import ru.example.project999.R
import ru.example.project999.core.CustomButton
import ru.example.project999.main.BaseFragment

class SubscriptionFragment :
    BaseFragment<SubscriptionRepresentative>(R.layout.fragment_suscription) {

    override val clasz = SubscriptionRepresentative::class.java
    /* private lateinit var subscriptionRepresentative: SubscriptionRepresentative

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
     }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("nn97", " SubscriptionFragment onViewCreated ")
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<CustomButton>(R.id.subscribe_button)
        button.setOnClickListener {
            representative.subscribe()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("nn97", "Subscription fragment onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Subscription fragment onPause")
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