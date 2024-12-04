package ru.example.project999.subscription

import android.util.Log
import ru.example.project999.core.HideAndShow
import java.io.Serializable

interface SubscriptionUiState : Serializable {

    //короче , когда мы делаем в репрезентативе data.observed(representative) -> observable.clear()->cache=empty
    //метод observed() чистит твой кэш, который в UiObservable() //он становится (SubscriptionUiState.Empty),кроме Лоадинга


    //по дефолту работает во всех, кроме лоадинга, то есть кэш там не чистится
    fun observed(representative: SubscriptionObserved) =
        representative.observed()//observable.clear()->cache=empty

    //это имеет смысл только если перед смертью у нас что-то было сохранено и восстановлено
    //, и мы просто не получили это в активити
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
            Log.d("nn91", "LoadingUistatesubscribeInner")
            representative.subscribeInner()
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
        ) = Unit


        //когда это вызовется , интересно ?
        //и когда мы сделали эмпти,
        //у нас и эмпти = Unit, то есть мы ниче делать не будем - не делаем лишнее действие ---КАКОЕ ?????
        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) = Unit
    }


}