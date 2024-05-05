package ru.example.project999.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import ru.example.project999.R
import ru.example.project999.core.CustomButton
import ru.example.project999.core.CustomTextView
import ru.example.project999.core.UiObserver
import ru.example.project999.main.BaseFragment

class DashboardFragment : BaseFragment<DashboardRepresentative>(R.layout.fragment_dashboard) {

    override val clasz = DashboardRepresentative::class.java

    init {
        Log.d("nn97", "DashboardFragment init")
    }

    private lateinit var callback: UiObserver<PremiumDashboardUiState>

    //  private lateinit var dashboardRepresentative: DashboardRepresentative

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    /*  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          dashboardRepresentative = (requireActivity() as ProvideRepresentative)
              .provideRepresentative(DashboardRepresentative::class.java)
      }*/

    /*    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(, container, false)
        }*/

    //здесь можно работать со вьюхой
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("nn97", "dashboard fragment`s onViewCreated")
        val button = view.findViewById<CustomButton>(R.id.playButton)
        val textView = view.findViewById<CustomTextView>(R.id.show_Playing_TextView)
        button.setOnClickListener {
            representative.play()
        }
        callback = object : UiObserver<PremiumDashboardUiState> {
            override fun update(data: PremiumDashboardUiState) {
                data.show(button, textView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("nn97", "dashboard fragment`s onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nn97", "dashboard fragment`s onDestroy")

    }

    override fun onResume() {
        super.onResume()
        Log.d("nn97", "Dashboard fragment onResume")
        representative.startGettingUpdates(callback)
    }

    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Dashboard fragment onPause")
        representative.stopGettingUpdates()
    }
}