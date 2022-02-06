package com.coolightman.note.util

import android.widget.RadioButton
import android.widget.RadioGroup

fun RadioGroup.getCheckedIndex(): Int {
    val checkedId = this.checkedRadioButtonId
    val radioButton = this.findViewById<RadioButton>(checkedId)
    return this.indexOfChild(radioButton)
}

fun RadioGroup.setCheckedByIndex(index: Int) {
    val radio = this.getChildAt(index) as RadioButton
    radio.isChecked = true
}