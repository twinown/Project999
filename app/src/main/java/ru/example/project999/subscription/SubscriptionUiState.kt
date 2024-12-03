package ru.example.project999.subscription

import ru.example.project999.core.HideAndShow
import java.io.Serializable

interface SubscriptionUiState : Serializable {

    //по дефолту работает во всех, кроме лоадинга, то есть кэш там не чистится
    fun observed(representative: SubscriptionObserved) =
        representative.observed()//observable.clear()->cache=empty

    //это имеет смысл только если перед смертью у нас что-то было сохранено и восстановлено
    //, и мы просто не получили это в активити
    fun restoreAfterDeath(
        representative: SubscriptionInner, observable: SubscriptionObservable
    ) = observable.update(this) //после равно - это типо по дефолту

    fun show(subscribeButton: HideAndShow, progressBar: HideAndShow, finishButton: HideAndShow)

    // TODO: КАК БУДЕТ ПОКАЗЫВАТЬСЯ Initial, ЕСЛИ ТАМ НЕТ НИГДЕ ПОСЛЕ РЕСТОРА observable.update(SubscriptionUiState.Initial)
    //тем более . что мы сделали потом
//--------------------------------------------------------------------------------------------------

    //ЗДЕСЬ ОПИСЫВАЕТСЯ, ЕСЛИ СМЕРТЬ ПРОИЗОШЛА НА ИНИШАЛЕ после НАЖАТИЯ КНОПКИ но до появления Loading`a
    //todo я всё равно не понял , зачем на инишале менять на эмпти.
    //если смерть произойдёт в моменте инишала. там же всегда будет вызываться ресторафтердет с эмпти,
    //а эмпти не делает ничего....как мы будет снова видеть инишал?????

//нажали кнопку
    //короче , когда мы делаем в репрезентативе data.observed(representative) -> observable.clear()->cache=empty
    //метод observed() чистит твой кэш, который в UiObservable() //он становится (SubscriptionUiState.Empty)
    //если смерть происходит на инишале, то перед этим в кэше лежит стейт инишиал
    //мы должны его зачистить, потому что после смерти будет restoreAfterDeath() у репрезентатива
    //он вызывается у uistate, который получили через метод restore().но получается, что при вызове
    //restoreAfterDeath() у инишла будет вызываться метод observable.update(this)(что выше-по умолчанию),НО у нас SubscriptionUiState.Empty
    // поэтому вызываться будет restoreAfterDeath() тот,что у эмпти (переопределённый)(который ниже, там стоит = Unit),то есть ничего не происходит


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


        //если после смерти остались в лоадинге, то пингует тред.старт еще раз при восстановлении
        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) {
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
        //у нас и эмпти = Unit, то есть мы ниче делать не будем - не делаем лишнее действие(показывание инишла,
        // потому что он и так покажется) каким ,блять, образом ??????
        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) = Unit
    }


}