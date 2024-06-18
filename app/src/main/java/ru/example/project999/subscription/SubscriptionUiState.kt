package ru.example.project999.subscription

import ru.example.project999.core.HideAndShow
import java.io.Serializable

interface SubscriptionUiState : Serializable {

    fun observed(representative: SubscriptionObserved) = representative.observed()

    fun restoreAfterDeath(
        representative: SubscriptionInner, observable: SubscriptionObservable
    ) = observable.update(this) //после равно - это типо по дефолту

    fun show(subscribeButton: HideAndShow, progressBar: HideAndShow, finishButton: HideAndShow)


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

        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) {
            representative.subscribeInner()
        }

        override fun observed(representative: SubscriptionObserved) = Unit
    }

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

    object Empty : SubscriptionUiState {

        override fun show(
            subscribeButton: HideAndShow,
            progressBar: HideAndShow,
            finishButton: HideAndShow
        ) = Unit

        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) = Unit
    }


}