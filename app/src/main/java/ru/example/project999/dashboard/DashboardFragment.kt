package ru.example.project999.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.example.project999.R
import ru.example.project999.core.ProvideRepresentative
import ru.example.project999.core.UiObserver

class DashboardFragment : Fragment() {

    private lateinit var callback:UiObserver<PremiumDashboardUiState>

    private lateinit var dashboardRepresentative: DashboardRepresentative

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardRepresentative = (requireActivity() as ProvideRepresentative)
            .provideRepresentative(DashboardRepresentative::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //здесь можно работать со вьюхой
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.playButton)
        val textView = view.findViewById<TextView>(R.id.show_Playing_TextView)
        button.setOnClickListener {
            dashboardRepresentative.play()
        }
        callback = object : UiObserver<PremiumDashboardUiState>{
            override fun update(data: PremiumDashboardUiState) {
                data.show(button, textView)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("nn970", "Dashboard fragment onResume")
        dashboardRepresentative.startGettingUpdates(callback)
    }

    override fun onPause() {
        super.onPause()
        Log.d("nn970", "Dashboard fragment onPause")
        dashboardRepresentative.stopGettingUpdates()
    }
}