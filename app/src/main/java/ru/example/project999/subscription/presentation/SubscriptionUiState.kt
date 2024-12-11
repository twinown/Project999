package ru.example.project999.subscription.presentation

import ru.example.project999.core.HideAndShow
import java.io.Serializable

interface SubscriptionUiState : Serializable {

    //короче , когда мы делаем в репрезентативе data.observed(representative) -> observable.clear()->cache=empty
    //метод observed() чистит твой кэш, который в UiObservable() //он становится (SubscriptionUiState.Empty),кроме Лоадинга


    //по дефолту работает во всех, кроме лоадинга, то есть кэш там не чистится
    fun observed(representative: SubscriptionObserved) =
        representative.observed()//observable.clear()->cache=empty


    //будет работать только у инишала и саксесса , если без observed()
    fun restoreAfterDeath(
        representative: SubscriptionInner, observable: SubscriptionObservable
    ) = observable.update(this) //после равно - это типо по дефолту

    fun show(subscribeButton: HideAndShow, progressBar: HideAndShow, finishButton: HideAndShow)

    //todo я всё равно не понял , зачем на инишале и саксессе менять на эмпти.


//--------------------------------------------------------------------------------------------------


    object Initial : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: HideAndShow,
            finishButton: HideAndShow
        ) {
            subscribeButton.show()
            progressBar.hide()
            finishButton.hide()
        }

    }

    //при restoreAfterDeath() у инишла будет вызываться метод observable.update(this)(что выше-по умолчанию)
    //то же и для observed()

    //--------------------------------------------------------------------------------------------------
    object Loading : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: HideAndShow,
            finishButton: HideAndShow
        ) {
            subscribeButton.hide()
            progressBar.show()
            finishButton.hide()
        }

        //если после смерти остались в лоадинге, то пингует тред.старт еще раз при восстановлении, иначе будет показываться только бублик(лоадинг)
        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) {

            representative.subscribeInner()
            //   Log.d("nn91", "LoadingUistatesubscribeInner")
        }

        override fun observed(representative: SubscriptionObserved) = Unit
    }

    //--------------------------------------------------------------------------------------------------
    object Success : SubscriptionUiState {
        override fun show(
            subscribeButton: HideAndShow,
            progressBar: HideAndShow,
            finishButton: HideAndShow
        ) {
            //     Log.d("nn97","шоу саксэса после смерти вызвался?или до смерти?")
            subscribeButton.hide()
            progressBar.hide()
            finishButton.show()
        }


    }

    //--------------------------------------------------------------------------------------------------
    object Empty : SubscriptionUiState {

        override fun show(
            subscribeButton: HideAndShow,
            progressBar: HideAndShow,
            finishButton: HideAndShow
        ): Unit {
            //   Log.d("nn97", "ниче не показываем типо..ёбаный бред")

        }

        override fun observed(representative: SubscriptionObserved) = Unit

        //когда это вызовется , интересно ?
        //и когда мы сделали эмпти,
        //у нас и эмпти = Unit, то есть мы ниче делать не будем - не делаем лишнее действие ---КАКОЕ ?????
        //отоброжание вьюх, андроид система сама их отображает после смерти
        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) = Unit
    }


}