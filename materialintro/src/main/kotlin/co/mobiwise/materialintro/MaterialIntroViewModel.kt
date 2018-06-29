package co.mobiwise.materialintro

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

class MaterialIntroViewModel : ViewModel() {

    val title = ObservableField<CharSequence?>()
    val desc = ObservableField<CharSequence?>()
}