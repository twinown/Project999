package ru.example.project999.core

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View

class CustomButton : androidx.appcompat.widget.AppCompatButton, HideAndShow {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState()?.let {
        val visibilityState = VisibilityState(it)
        visibilityState.visible = visibility
        return visibilityState
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.d("nn97", "или здесь после смерти чё-то")
        val visibilityState = state as VisibilityState?
        super.onRestoreInstanceState(visibilityState?.superState)
        visibilityState?.let {
            visibility = it.visible
        }
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}

class VisibilityState : View.BaseSavedState {
    var visible: Int = View.VISIBLE

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        visible = parcelIn.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(visible)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<VisibilityState> {
        override fun createFromParcel(parcel: Parcel): VisibilityState = VisibilityState(parcel)
        override fun newArray(size: Int): Array<VisibilityState?> = arrayOfNulls(size)
    }
}