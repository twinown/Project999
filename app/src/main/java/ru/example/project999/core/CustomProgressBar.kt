package ru.example.project999.core

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar

class CustomProgressBar : ProgressBar, HideAndShow {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState()?.let {
        val visibilityState = VisibilityState(it)
        visibilityState.visible = visibility
        return visibilityState
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        val visibilityState = state as VisibilityState?
        super.onRestoreInstanceState(visibilityState?.superState)
        visibilityState?.let {
            visibility = it.visible
        }
    }
}