package ru.example.project999.dashboard

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

    private lateinit var observer: UiObserver<PremiumDashboardUiState> //для стейта

    //  private lateinit var dashboardRepresentative: DashboardRepresentative


    //раньше было так
    /*  override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          dashboardRepresentative = (requireActivity() as ProvideRepresentative)
              .provideRepresentative(DashboardRepresentative::class.java)
      }*/

    //он не нужен ,потому что в baseFragment в конструктор кидается твой id макета фрагмента
    /*    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(container, false)
        }*/

    //здесь можно работать со вьюхой
    //This is the appropriate place to set up the initial state of your view,
    // to start observing LiveData instances whose callbacks update the fragment's
    // view, and to set up adapters on any RecyclerView or ViewPager2 instances in
    // your fragment's view.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("nn97", "dashboard fragment`s onViewCreated")
        val button = view.findViewById<CustomButton>(R.id.playButton)
        val textView = view.findViewById<CustomTextView>(R.id.show_Playing_TextView)
        button.setOnClickListener {
            representative.play()
        }

        //держит ссылку на тот класс, в котором находится
        observer = object : DashboardObserver {
            //эт вызывается в uiobservable,когда уже премиум только, у обсервера, коим является твой фрагмент
            //  observable.update(PremiumDashboardUiState.Playing)->  observer.update(data)
            //короче , выше нажимается плей, там внутри вызывается апдейт, что ниже,если премиум, в нем вызывается
            // шоу из премиумдэшбордюайстейта
            //если не прем то вызывается  navigation.update(SubscriptionScreen), сюда не заходя
            override fun update(data: PremiumDashboardUiState) {
                data.observed(representative)
                data.show(button, textView)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("nn97", "Dashboard fragment onResume")
        //дёргаются коллбэки
        //зачем метод этот здесь ?
        //фрагмент дали репрезентативу
        //здесь репрезентатив получает доступ к фрагменту
        //здесть репрезентатив ака аппликашн держит ссылку на фрагмент
        //я репрезентативу даю доступ к активити

        //он не переопределён в дашбордрепрезентативе В БАЗЕ, а в базовом интерфейсе репрезентатива
        //ЮНИТ и написано
        ////юнит - для тех репрезентативов и их фрагментов. которым  не нужно обновлять себя!!!!!
        //потому в если бэйз, то ничё не происходит
        representative.startGettingUpdates(observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("nn97", "dashboard fragment`s onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nn97", "dashboard fragment`s onDestroy")

    }


    override fun onPause() {
        super.onPause()
        Log.d("nn97", "Dashboard fragment onPause")
        representative.stopGettingUpdates()
    }

    interface DashboardObserver : UiObserver<PremiumDashboardUiState>


}