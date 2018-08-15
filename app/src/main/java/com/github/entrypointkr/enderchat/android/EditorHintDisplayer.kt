package com.github.entrypointkr.enderchat.android

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Created by JunHyeong on 2018-08-14
 */
class EditorHintDisplayer(
        private val layout: TextInputLayout,
        private val mockHint: CharSequence
) : View.OnFocusChangeListener {
    private val actualHint = layout.hint

    init {
        layout.hint = mockHint
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            layout.hint = actualHint
        } else if (v is EditText && v.text.isBlank()) {
            layout.hint = mockHint
        }
    }
}
