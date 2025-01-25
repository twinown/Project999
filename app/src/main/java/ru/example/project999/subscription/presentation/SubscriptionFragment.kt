package ru.example.project999.subscription.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import ru.example.project999.R
import ru.example.project999.core.CustomButton
import ru.example.project999.core.CustomProgressBar
import ru.example.project999.core.UiObserver
import ru.example.project999.main.BaseFragment

class SubscriptionFragment :

    BaseFragment<SubscriptionRepresentative>(R.layout.fragment_suscription) {

    override val clasz = SubscriptionRepresentative::class.java

    init {
        Log.d("nn97", "Subsragment init")
    }

    private lateinit var observer: UiObserver<SubscriptionUiState>//для стейта

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("nn97", " SubscriptionFragment onViewCreated ")
        //они сами знают.как сохранять свою визибилити
        val subscribeButton = view.findViewById<CustomButton>(R.id.subscribe_button)
        val progressBar = view.findViewById<CustomProgressBar>(R.id.progressBar)
        val finishButton = view.findViewById<CustomButton>(R.id.finish_button)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                representative.comeBack()
            }

        })
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
            override fun update(data: SubscriptionUiState)
            //было так до корутин
            //   = requireActivity().runOnUiThread
            {
                    Log.d("nn97", "(на он резюме) сабскрип фрагма (-после смерти) $data")
                    //этот метод вызывается из другого потока, потому вот так
                    //разобраться в этом методе,нах он
                    //да, И я не понял, нафига кэш=эмпти здесь для инишала и для саксесса
                    // метод ниже нужен для того,чтобы сказать репрезентативу, что я реально получил стейт
                    //короче, Нияз, этот метод нужен для того. чтоб на он резюме после смерти не вызывался метод update() с инишалом и с саксессом
                    // стейтами , так как андроид система после смерти и так восстановит активити твою и фрагмент автоматически вместе с ними
                    // на том моменте, где ты был..вьюхи восстанавливаются.
                    //надо сохранять только лоадинг, и то не весь. крутилка также восттановится , как и юайки выше
                    //просто на ресторе в лоадинге мы снова запускаем тред, посмотри
                    //подробная работа в .txt файле 78
                    // это код делает SubscriptionUiState.Empty в UiObservable (cache=empty) для инишла и саксесса
                    data.observed(representative) //на лоадинге ничего не делает(юнит)
                    Log.d(
                        "nn97",
                        "(на он резюме) сабскрип фрагма после обзервда (-после смерти) $data"
                    )
                    data.show(subscribeButton, progressBar, finishButton)
                    //todo data - это ? -> мб SubscriptionUiState.Loading||Initial||Success #DONE
                }
        }

        //хэндл смерти процесса
        //здесь нуллабл
        //при первом запуске здесь в кэш записывается -> Initial observable.update(SubscriptionUiState.Initial)->но observer.update(data)
        //то есть метод выше , не вызывается (при первом запуске обзервер еще эмпти), поэтому показывание происходит с онРезюма
        //кэш сделали инишал, потому что когда на он резюме вызывается апдейтобзервер. то мы вызываем там   observer.update(cache), который кэш
        representative.init(SaveAndRestoreSubscriptionUiState.Base(savedInstanceState))
        //сделали обёртку в инт-се SaveAndRestoreState
        //чтоб можно было с бандлом работать не только тут, тк бандл - это андроид ос
        //чтоб можно было в инит принимать бандл - сделали обёртку для него
    }

    //сохранение стейта всего экрана перед смертью//здесь не нуллабл
    //КОГДА ОН ВЫЗЫВАЕТСЯ?????
    //И ЧТО ВООБЩЕ СОХРАНЯЕТСЯ В БАНДЛ ??->UiState-сохраняется только Loading, потому что остальные стейты нет смысла сохранять
    //они и так восстановятся
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("nn97", "Subscription fragment`s onSaveInstanceState called")
        representative.save(SaveAndRestoreSubscriptionUiState.Base(outState))
    }


    //здесь уже вызывается выше метод выше с кэшэм инишала
    //то есть при первом открытии записывается только в кэш инишал, там не вызыватся апдейт обзервера(выше)
    //апдейфт обзервера вызывается вот на методе observable.updateObserver()-> observer.update(cache)
    // то же с саксессом
    override fun onResume() {
        super.onResume()
        Log.d("nn97", " после смерти на лодинге вызвался он резюм - вызывается после смерти всегда")
        Log.d("nn97", "Subscription fragment onResume")
        representative.startGettingUpdates(observer) //observable.updateObserver()->observer.update(cache)
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

    interface SubscriptionObserver : UiObserver<SubscriptionUiState>
}