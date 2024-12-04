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
        //они сами знают.как сохранять свою визибилити
        val subscribeButton = view.findViewById<CustomButton>(R.id.subscribe_button)
        val progressBar = view.findViewById<CustomProgressBar>(R.id.progressBar)
        val finishButton = view.findViewById<CustomButton>(R.id.finish_button)
        subscribeButton.setOnClickListener {
            representative.subscribe()
        }
        finishButton.setOnClickListener {
            representative.finish()
        }

        //TODO ПРАВИЛЬНО ТАК ??? КАК ЖЕ БУДЕТ ВЫПОЛНЯТЬСЯ МЕТОД finish()????ответ->навигашн всегда один и тот же #DONE
        observer = object : SubscriptionObserver {
            //будет дёргаться из subscribe() - обновление ui
            //это метод вызывается в первый раз на OnResume()->//observable.updateObserver(), там внтури ->
            //-> вызывается   observer.update(cache), где кэш уже SubscriptionUiState.Initial
            override fun update(data: SubscriptionUiState) =
                requireActivity().runOnUiThread { //этот метод вызывается из другого потока, потому вот так
                    //разобраться в этом методе,нах он
                    //да, И я не понял, нафига кэш=эмпти здесь для инишала и для саксесса
                    // метод ниже нужен для того,чтобы сказать репрезентативу, что я реально получил стейт
                    // это код делает SubscriptionUiState.Empty в UiObservable (cache=empty) для инишла и саксесса
                    data.observed(representative)
                    data.show(subscribeButton, progressBar, finishButton)
                    //todo data - это ? -> мб SubscriptionUiState.Loading||Initial||Success #DONE
                }
        }

        //хэндл смерти процесса
        //здесь нуллабл
        //при первом запуске здесь в кэш записывается Initial
        representative.init(SaveAndRestoreSubscriptionUiState.Base(savedInstanceState))
        //сделали обёртку в инт-се SaveAndRestoreState
        //чтоб можно было с бандлом работать не только тут, тк бандл - это андроид ос
        //чтоб можно было в инит принимать бандл - сделали обёртку для него
    }

    //сохранение стейта всего экрана перед смертью//здесь не нуллабл
    //КОГДА ОН ВЫЗЫВАЕТСЯ?????
    //И ЧТО ВООБЩЕ СОХРАНЯЕТСЯ В БАНДЛ ??->UiState
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("nn97", "Subscription fragment`s onSaveInstanceState called")
        representative.save(SaveAndRestoreSubscriptionUiState.Base(outState))
    }

    override fun onResume() {
        super.onResume()
        Log.d("nn97", "Subscription fragment onResume")
        representative.startGettingUpdates(observer) //observable.updateObserver()
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

    interface SubscriptionObserver : UiObserver<SubscriptionUiState> {
        override fun isEmpty(): Boolean = false
    }
}